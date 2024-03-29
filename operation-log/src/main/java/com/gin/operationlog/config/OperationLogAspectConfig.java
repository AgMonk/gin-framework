package com.gin.operationlog.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.common.utils.ParamArg;
import com.gin.spring.utils.SpElUtils;
import com.gin.spring.utils.SpringContextUtils;
import com.gin.spring.utils.WebUtils;
import com.gin.spring.vo.response.Res;
import com.gin.operationlog.annotation.LogStrategy;
import com.gin.operationlog.annotation.OpLog;
import com.gin.operationlog.bo.OperationLogContext;
import com.gin.operationlog.entity.SystemOperationLog;
import com.gin.operationlog.enums.OperationType;
import com.gin.operationlog.service.SystemOperationLogService;
import com.gin.operationlog.service.UserInfoService;
import com.gin.operationlog.strategy.DescriptionStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 操作日志切面配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 09:49
 */
@Configuration
@Aspect
@Slf4j
@RequiredArgsConstructor
public class OperationLogAspectConfig {
    private final SystemOperationLogService logService;
    private final UserInfoService userInfoService;

    /**
     * 查找匹配的策略
     * @param entityClass 操作的实体类型
     * @return 策略
     */
    private static List<DescriptionStrategy> findStrategies(Class<?> entityClass, OperationType type) {
        return SpringContextUtils.getContext().getBeansOfType(DescriptionStrategy.class).values().stream().filter(s -> {
            // 过滤出有注解的，操作类型匹配的，实体类型包含的策略
            final LogStrategy annotation = s.getClass().getAnnotation(LogStrategy.class);
            return annotation != null && type.equals(annotation.type()) && annotation.value().isAssignableFrom(entityClass);
        }).sorted((o1, o2) -> {
            // 排序 ，子类在前
            final Class<?> c1 = o1.getClass().getAnnotation(LogStrategy.class).value();
            final Class<?> c2 = o2.getClass().getAnnotation(LogStrategy.class).value();
            if (c1.equals(c2)) {
                return 0;
            }
            if (c1.isAssignableFrom(c2)) {
                return 1;
            }
            if (c2.isAssignableFrom(c1)) {
                return -1;
            }
            return 0;
        }).toList();
    }

    /**
     * 生成请求参数描述
     * @param context 上下文
     * @return 请求参数描述
     */
    private static String getRequestParam(OperationLogContext context) {
        List<Class<?>> classes = List.of(HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
        final ObjectMapper mapper = new ObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);

        HashMap<String, Object> map = new HashMap<>();

        context.paramArgs().stream().filter(f -> !classes.contains(f.getParameter().getType())).forEach(paramArg -> map.put(paramArg.getParameter().getName(),
                                                                                                                         paramArg.getArg()));
        try {
            return mapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * 生成返回结果描述
     * @param context 上下文
     * @return 返回结果描述
     */
    private static String getResponseResult(OperationLogContext context) {
        final ObjectMapper mapper = new ObjectMapper().setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL);
        try {
            final Object result = context.result();
            if (result == null) {
                return null;
            }
            if (result instanceof Res<?> res) {
                return mapper.writeValueAsString(res.getData());
            }
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    @Around("@annotation(opLog)")
    public Object around(ProceedingJoinPoint pjp, OpLog opLog) throws Throwable {
        final long start = now();
        final long currentUserId = userInfoService.getCurrentUserId();
        //静态方法获取 HttpServletRequest
        final HttpServletRequest request = WebUtils.getHttpServletRequest();
        final HttpSession session = request != null ? request.getSession() : null;
        // 请求参数和参数值
        final List<ParamArg> paramArgs = parse(pjp);
        final Class<?> mainClass = opLog.mainClass();
        // 副类型如果为 object 置为null
        final Class<?> subClass = !Object.class.equals(opLog.subClass()) ? opLog.subClass() : null;
        // 操作类型
        final OperationType type = opLog.type();

        final StandardEvaluationContext evaluationContext = SpElUtils.createContext(pjp);
        evaluationContext.setVariable("currentUserId", currentUserId);

        final List<Object> preExp = SpElUtils.getElValues(evaluationContext, opLog.preExp());

        final Object result = pjp.proceed();

        // SpEl上下文
        evaluationContext.setVariable("result", result);
        // 计算 SpEl表达式
        final List<Object> sufExp = SpElUtils.getElValues(evaluationContext, opLog.sufExp());
        final Long mainId = SpElUtils.getElNotnullLong(evaluationContext, opLog.mainId()).stream().findFirst().orElse(null);
        final Long subId = subClass != null ? SpElUtils.getElNotnullLong(evaluationContext, opLog.subId()).stream().findFirst().orElse(null) : null;
        if (mainId == null) {
            log.warn("日志注解配置错误: mainId 计算结果为 null");
            return result;
        }
        // 计算其他表达式


        // 匹配描述策略
        // 实际操作的实体类对象，用于匹配策略
        final Class<?> entityClass = subClass != null ? subClass : mainClass;
        final Long entityId = subClass != null ? subId : mainId;

        // 上下文
        final OperationLogContext context = new OperationLogContext(entityClass, entityId, paramArgs, result, preExp, sufExp, type, request);
        // 日志
        final SystemOperationLog operationLog = new SystemOperationLog();
        operationLog.setSessionId(session != null ? session.getId() : null);
        operationLog.setType(type);
        operationLog.setUserId(currentUserId);
        operationLog.setUserIp(WebUtils.getRemoteHost(request));
        operationLog.setMainClass(mainClass);
        operationLog.setMainId(mainId);
        operationLog.setSubClass(subClass);
        operationLog.setSubId(subId);
        operationLog.setRequestParam(opLog.requestParam() ? getRequestParam(context) : null);
        operationLog.setResponseResult(opLog.responseResult() ? getResponseResult(context) : null);

        //描述生成策略
        final List<DescriptionStrategy> strategies = findStrategies(entityClass, type);
        // 如果策略非空，尝试使用策略生成描述
        if (!CollectionUtils.isEmpty(strategies)) {
            for (DescriptionStrategy strategy : strategies) {
                final Class<?> strategyClass = strategy.getClass().getAnnotation(LogStrategy.class).value();
                // 生成描述
                final String description = strategy.generateDescription(context);
                // 输出的描述非空 则应用
                if (!ObjectUtils.isEmpty(description)) {
                    if (!strategyClass.equals(entityClass)) {
                        log.debug("非专用策略 策略:{} 实体:{}", strategyClass, entityClass);
                    }
                    operationLog.setStrategyClass(strategy.getClass());
                    operationLog.setDescription(description);
                    operationLog.setTimeCost(now() - start);
                    logService.write(operationLog);
                    return result;
                }
            }
        }

        final String msg = "未找到匹配的描述策略";
        final String des = String.format("%s class:%s type:%s mainId:%s", msg, entityClass, type, mainId);
        log.warn(des);
        operationLog.setDescription(msg);
        operationLog.setTimeCost(now() - start);
        logService.write(operationLog);
        return result;
    }
    public static List<ParamArg> parse(ProceedingJoinPoint pjp) {
        //签名
        final MethodSignature signature = (MethodSignature) pjp.getSignature();
        //方法
        final Method method = signature.getMethod();
        // 组合
        return ParamArg.merge(method.getParameters(), pjp.getArgs());
    }
}
