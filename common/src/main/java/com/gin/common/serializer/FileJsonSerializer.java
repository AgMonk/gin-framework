package com.gin.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.File;
import java.io.IOException;

/**
 * 文件序列化
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/19 17:07
 */
public class FileJsonSerializer extends JsonSerializer<File> {
    @Override
    public void serialize(File value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeString(value.getPath());
    }
}
