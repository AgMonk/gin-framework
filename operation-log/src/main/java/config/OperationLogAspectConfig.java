package config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.springboot3template.operationlog.annotation.LogStrategy;
import com.gin.springboot3template.operationlog.annotation.OpLog;
import com.gin.springboot3template.operationlog.bo.OperationLogContext;
import com.gin.springboot3template.operationlog.entity.SystemOperationLog;
import com.gin.springboot3template.operationlog.enums.OperationType;
import com.gin.springboot3template.operationlog.service.SystemOperationLogService;
import com.gin.springboot3template.operationlog.strategy.DescriptionStrategy;
import com.gin.springboot3template.sys.utils.ParamArg;
import com.gin.springboot3template.sys.utils.SpElUtils;
import com.gin.springboot3template.sys.utils.SpringContextUtils;
import com.gin.springboot3template.sys.utils.WebUtils;
import com.gin.springboot3template.sys.vo.response.Res;
import com.gin.springboot3template.user.entity.SystemUser;
import com.gin.springboot3template.user.security.bo.MyUserDetails;
import com.gin.springboot3template.user.security.utils.MySecurityUtils;
import com.gin.springboot3template.user.service.SystemUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

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
    private final SystemUserService systemUserService;

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

        context.paramArgs().stream().filter(f -> !classes.contains(f.parameter().getType())).forEach(paramArg -> map.put(paramArg.parameter().getName(),
                                                                                                                         paramArg.arg()));
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
        //静态方法获取 HttpServletRequest
        final HttpServletRequest request = WebUtils.getHttpServletRequest();
        final HttpSession session = request != null ? request.getSession() : null;
        // 权限框架获取 userId
        final MyUserDetails userDetails = MySecurityUtils.currentUserDetails();
        // 请求参数和参数值
        final List<ParamArg> paramArgs = ParamArg.parse(pjp);
        final Class<?> mainClass = opLog.mainClass();
        // 副类型如果为 object 置为null
        final Class<?> subClass = !Object.class.equals(opLog.subClass()) ? opLog.subClass() : null;
        // 操作类型
        final OperationType type = opLog.type();

        final StandardEvaluationContext evaluationContext = SpElUtils.createContext(pjp);
        evaluationContext.setVariable("userDetail", MySecurityUtils.currentUserDetails());

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
        operationLog.setUserId(userDetails.getId());
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

    /**
     * 登陆方法环绕切面
     * @param pjp pjp
     * @return 返回
     */
    @Around(value = "execution(* org.springframework.security.authentication.AuthenticationManager.authenticate(..)) && args(token)", argNames = "pjp,token")
    public Object login(ProceedingJoinPoint pjp, UsernamePasswordAuthenticationToken token) throws Throwable {
        if (ProviderManager.class.equals(pjp.getTarget().getClass())
                && token.getDetails() instanceof WebAuthenticationDetails details
        ) {
            final long start = now();
            final String userIp = details.getRemoteAddress();
            try {
                //登陆成功
                final Authentication result = (Authentication) pjp.proceed();
                if (result.isAuthenticated()) {
                    MyUserDetails principal = (MyUserDetails) result.getPrincipal();
                    final SystemOperationLog operationLog = new SystemOperationLog();
                    final Long userId = principal.getId();
                    operationLog.setMainClass(SystemUser.class);
                    operationLog.setSessionId(details.getSessionId());
                    operationLog.setType(OperationType.LOGIN);
                    operationLog.setUserIp(userIp);
                    operationLog.setUserId(userId);
                    operationLog.setMainId(userId);
                    operationLog.setDescription("登录成功");
                    operationLog.setTimeCost(now() - start);
                    logService.write(operationLog);
                }
                return result;
            } catch (Throwable e) {
                // 登陆失败
                final SystemUser user = systemUserService.getByUsername((String) token.getPrincipal());
                final long userId = user != null ? user.getId() : -1;
                final SystemOperationLog operationLog = new SystemOperationLog();
                operationLog.setMainClass(SystemUser.class);
                operationLog.setSessionId(details.getSessionId());
                operationLog.setType(OperationType.LOGIN_FAILED);
                operationLog.setUserIp(userIp);
                operationLog.setUserId(userId);
                operationLog.setMainId(userId);
                operationLog.setDescription(e.getLocalizedMessage());
                operationLog.setTimeCost(now() - start);
                logService.write(operationLog);
                throw e;
            }
        }
        return pjp.proceed();
    }

    @Around(value = "execution(* org.springframework.security.web.authentication.logout.LogoutSuccessHandler.onLogoutSuccess(..)) && args(request,response,token)"
            , argNames = "pjp,request,response,token")
    public Object logout(
            ProceedingJoinPoint pjp,
            HttpServletRequest request,
            HttpServletResponse response,
            UsernamePasswordAuthenticationToken token
    ) throws Throwable {
        if (token.getDetails() instanceof WebAuthenticationDetails details) {
            final long start = now();
            MyUserDetails principal = (MyUserDetails) token.getPrincipal();
            final Long userId = principal.getId();

            final Object result = pjp.proceed();

            final SystemOperationLog operationLog = new SystemOperationLog();
            operationLog.setMainClass(SystemUser.class);
            operationLog.setSessionId(details.getSessionId());
            operationLog.setType(OperationType.LOGOUT);
            operationLog.setUserIp(WebUtils.getRemoteHost(request));
            operationLog.setUserId(userId);
            operationLog.setMainId(userId);
            operationLog.setDescription("登出成功");
            operationLog.setTimeCost(now() - start);
            logService.write(operationLog);
            return result;
        }
        return pjp.proceed();
    }
}
