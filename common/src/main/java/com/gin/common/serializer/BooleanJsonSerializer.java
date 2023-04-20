package com.gin.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * 布尔值序列化
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/20 12:04
 */
public class BooleanJsonSerializer extends JsonSerializer<Boolean> {
    @Override
    public void serialize(Boolean value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (Boolean.TRUE.equals(value)) {
            gen.writeString("1");
        }
        if (Boolean.FALSE.equals(value)) {
            gen.writeString("0");
        }
    }
}
