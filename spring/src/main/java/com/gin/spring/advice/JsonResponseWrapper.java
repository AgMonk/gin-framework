package com.gin.spring.advice;

import com.gin.spring.annotation.RestWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.gin.common.vo.response.Res;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 自动包装响应体
 * {@link ControllerAdvice} 的 basePackages属性限制了生效的包路径, 避免与swagger 冲突
 * @author bx002
 */
@ControllerAdvice(basePackages = {
        "com.gin",
        "com.baixun",
})
@RequiredArgsConstructor
@Slf4j
public class JsonResponseWrapper implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper;

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response
    ) {
        // 如果结果已经被包装过，直接返回
        if (body instanceof Res) {
            return body;
        }

        // 如果方法或类上存在注解,且disable字段为true 直接返回
        final RestWrapper a1 = returnType.getMethodAnnotation(RestWrapper.class);
        final RestWrapper a2 = returnType.getDeclaringClass().getAnnotation(RestWrapper.class);
        final RestWrapper restWrapper = a1 == null ? a2 : a1;
        if (restWrapper != null && restWrapper.disable()) {
            return body;
        }
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);

        // 包装结果
        final Res<Object> res = Res.of(body);

        // 如果结果集是 String 类型，要提前进行序列化操作，否则会包装失败
        if (returnType.getParameterType() == String.class) {
            try {
                return objectMapper.writeValueAsString(res);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        // 返回结果
        return res;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }
}