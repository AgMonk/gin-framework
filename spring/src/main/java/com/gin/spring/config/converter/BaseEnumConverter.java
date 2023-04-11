package com.gin.spring.config.converter;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.core.convert.converter.Converter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 枚举对象反序列化转换器
 * @author bx002
 */

public class BaseEnumConverter<T extends Enum<?>> implements Converter<String, T> {
    private final Map<String, T> enumMap = new HashMap<>();

    /**
     * 通过枚举类对象构造
     * @param enumType 枚举类对象
     */
    public BaseEnumConverter(Class<T> enumType) {
        // 枚举对象
        T[] enums = enumType.getEnumConstants();

        // 枚举类中以 JsonValue 注解的字段
        final List<Field> fields = Arrays.stream(enumType.getDeclaredFields()).filter(i -> i.getAnnotation(JsonValue.class) != null).toList();
        // 枚举类中以 JsonValue 注解的方法
        final List<Method> methods = Arrays.stream(enumType.getDeclaredMethods()).filter(i -> i.getAnnotation(JsonValue.class) != null).toList();

        for (T e : enums) {
            //将枚举序号作为key放入map
            enumMap.put(String.valueOf(e.ordinal()), e);
            //将枚举名称作为key放入map
            enumMap.put(e.name(), e);
            //将 JsonValue 注解的字段值作为key放入map
            fields.forEach(f -> {
                try {
                    f.setAccessible(true);
                    enumMap.put(String.valueOf(f.get(e)), e);
                    f.setAccessible(false);
                } catch (IllegalAccessException ignored) {
                }
            });
            //将 JsonValue 注解的方法返回值作为key放入map
            methods.forEach(m -> {
                try {
                    enumMap.put(String.valueOf(m.invoke(e)), e);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            });
        }
    }

    @Override
    public T convert(String source) {
        T t1 = enumMap.get(source.toLowerCase());
        T t2 = enumMap.get(source.toUpperCase());
        if (t1 == null && t2 == null) {
            throw new IllegalArgumentException("无法匹配对应的枚举类型");
        }
        return t1 == null ? t2 : t1;
    }
}