package com.gin.spring.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.common.utils.StrUtils;
import com.gin.jackson.utils.JacksonUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * SpEl表达式工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/21 09:51
 */
@Slf4j
public class SpElUtils {

    /**
     * 生成表达式上下文
     * @param joinPoint 接触点
     * @return spEl表达式上下文
     */
    public static StandardEvaluationContext createContext(JoinPoint joinPoint) {
        final ApplicationContext applicationContext = SpringContextUtils.getContext();
        final StandardEvaluationContext context = new StandardEvaluationContext(applicationContext);

        Object[] args = joinPoint.getArgs();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        StandardReflectionParameterNameDiscoverer parameterNameDiscoverer = new StandardReflectionParameterNameDiscoverer();
        String[] parametersName = parameterNameDiscoverer.getParameterNames(targetMethod);

        if (args == null || args.length == 0) {
            return context;
        }
        for (int i = 0; i < args.length; i++) {
            //noinspection DataFlowIssue
            context.setVariable(parametersName[i], args[i]);
        }
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
        return context;
    }

    /**
     * 批量计算表达式，过滤掉null值，且仅保留Long类型
     * @param spEl    表达式
     * @param context 上下文
     * @return 计算结果
     */
    public static List<Long> getElNotnullLong(StandardEvaluationContext context, String... spEl) {
        return getElValues(context, spEl).stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .filter(StrUtils::isNumber)
                .map(Long::parseLong)
                .toList();
    }

    /**
     * 批量计算表达式(过滤掉null值)
     * @param spEl    表达式
     * @param context 上下文
     * @return 计算结果
     */
    public static List<Object> getElNotnullValues(StandardEvaluationContext context, String... spEl) {
        return getElValues(context, spEl).stream().filter(Objects::nonNull).toList();
    }

    /**
     * 计算表达式
     * @param context 上下文
     * @param spEl    表达式
     * @return 计算结果
     */
    public static Object getElValue(StandardEvaluationContext context, String spEl) {
        return ObjectUtils.isEmpty(spEl) ? null : new SpelExpressionParser().parseExpression(spEl).getValue(context);
    }

    /**
     * 批量计算表达式
     * @param spEl    表达式
     * @param context 上下文
     * @return 计算结果
     */
    public static List<Object> getElValues(StandardEvaluationContext context, String... spEl) {
        return Arrays.stream(spEl).map(e -> {
            try {
                return getElValue(context, e);
            } catch (SpelEvaluationException ex) {
                log.warn("表达式计算异常: {} : {}",e,ex.getLocalizedMessage());
                return null;
            }
        }).toList();
    }
}
