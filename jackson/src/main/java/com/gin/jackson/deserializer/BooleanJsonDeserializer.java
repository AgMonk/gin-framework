package com.gin.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

/**
 * 布尔值反序列化
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/19 17:08
 */
public class BooleanJsonDeserializer extends JsonDeserializer<Boolean> {
    @Override
    public Boolean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        final String value = p.getValueAsString();
        if ("1".equals(value)){
            return true;
        }
        if ("true".equals(value)){
            return true;
        }
        if ("false".equals(value)){
            return false;
        }
        if ("0".equals(value)){
            return false;
        }
        return null;
    }
}
