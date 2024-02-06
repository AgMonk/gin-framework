package com.gin.jackson.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/**
 * json工具类
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 15:44
 */
public class JacksonUtils {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public final static ObjectMapper MAPPER = getMapper();

    public static ObjectMapper getMapper() {
        return new ObjectMapper()
                .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                //美化输出
                .enable(SerializationFeature.INDENT_OUTPUT)
                //反序列化时 空串识别为 null
                .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
                // 反序列化时,遇到未知属性会不会报错
                .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
                //支持 ZonedDateTime
                .registerModule(new JavaTimeModule());
    }

    /**
     * 把对象转换为HashMap
     *
     * @param obj 对象 推荐使用HashMap ，传入null的字段会传递空串
     * @return HashMap
     */
    public static HashMap<String, Object> jsonToMap(Object obj) {
        try {
            return MAPPER.readValue(MAPPER.writeValueAsString(obj), new TypeReference<HashMap<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            return new HashMap<>(0);
        }
    }

    /**
     * 获取泛型的Collection Type
     *
     * @param containerClass 容器类型
     * @param elementClasses 泛型类型
     * @return JavaType Java类型
     * @since 1.0
     */
    public static JavaType obtainJavaType(Class<?> containerClass, Class<?>... elementClasses) {
        return MAPPER.getTypeFactory().constructParametricType(containerClass, elementClasses);
    }

    public static <T> T parseObj(Object obj, Class<T> clazz) throws JsonProcessingException {
        return MAPPER.readValue(MAPPER.writeValueAsString(obj), clazz);
    }

    /**
     * 美化输出
     *
     * @param obj 对象
     */
    public static void printPretty(Object obj) {
        try {
            System.out.println(MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 单行输出
     *
     * @param obj 对象
     */
    public static void print(Object obj) {
        try {
            System.out.println(getMapper().disable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}   
