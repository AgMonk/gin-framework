package com.gin.redis;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gin.common.utils.ParamArg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 缓存注解使用的Key生成器, 在 {@link org.springframework.cache.annotation.CacheConfig} 等上使用
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/24 10:42
 */
@Component(CustomKeyGenerator.NAME)
public class CustomKeyGenerator implements KeyGenerator {
    public static final String NAME = "custom_key_generator";
    /**
     * 需要排除的参数类型
     */
    private static final List<Class<?>> EXCLUDED_CLASSES = List.of(HttpServletRequest.class, HttpServletResponse.class, HttpSession.class);
    private static final ObjectMapper MAPPER = new Jackson2ObjectMapperBuilder().serializationInclusion(JsonInclude.Include.NON_NULL)
            .build();
    private static final String SPLIT = ":";

    public static String valueOf(Object obj) {
        try {
            return MAPPER.writeValueAsString(obj).replace(":", "=");
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    /**
     * Generate a key for the given method and its parameters.
     *
     * @param target 被调用方法的类实例
     * @param method 被调用的方法
     * @param params 参数列表
     * @return a generated key
     */
    @Override
    public String generate(Object target, Method method, Object... params) {
        // 被调用方法的类的简单名
        final String targetSimpleName = target.getClass().getSimpleName();
        // 方法名
        final String methodName = method.getName();
        // 生成简单key
        final String simpleKey = targetSimpleName + SPLIT + methodName;
        if (params.length == 0) {
            return simpleKey;
        }
        // 组合成参数+参数值列表
        final List<ParamArg> paramArgs = ParamArg.merge(method.getParameters(), params).stream()
                .filter(f -> !EXCLUDED_CLASSES.contains(f.getParameter().getType())).toList();
        if (CollectionUtils.isEmpty(paramArgs)) {
            return simpleKey;
        }
        // 有参数
        return simpleKey + SPLIT + paramArgs.stream()
                .map(paramArg -> paramArg.getParameter().getName() + "=" + valueOf(paramArg.getArg()))
                .collect(Collectors.joining(";"));
    }
}
