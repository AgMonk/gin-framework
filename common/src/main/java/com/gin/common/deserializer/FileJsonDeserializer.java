package com.gin.common.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.ObjectUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件反序列化
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/19 17:08
 */
public class FileJsonDeserializer extends JsonDeserializer<File> {
    @Override
    public File deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JacksonException {
        final String value = p.getValueAsString();
        if (ObjectUtils.isEmpty(value)) {
            return null;
        }
        return new File(value);
    }
}
