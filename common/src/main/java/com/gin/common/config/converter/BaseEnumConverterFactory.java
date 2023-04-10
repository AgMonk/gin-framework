package com.gin.common.config.converter;

import jakarta.validation.constraints.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 枚举转换器工厂
 * @author bx002
 */
public class BaseEnumConverterFactory implements ConverterFactory<String, Enum<?>> {
    private static final Map<Class<?>, Converter> CONVERTERS = new HashMap<>();


    @NotNull
    @Override
    public <T extends Enum<?>> Converter<String, T> getConverter(@NotNull Class<T> targetType) {
        // 每一个类型创建一个转换器
        Converter<String, T> converter = CONVERTERS.get(targetType);
        if (converter == null) {
            converter = new BaseEnumConverter<>(targetType);
            CONVERTERS.put(targetType, converter);
        }
        return converter;
    }
}