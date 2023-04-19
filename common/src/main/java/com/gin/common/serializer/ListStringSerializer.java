package com.gin.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;

/**
 * 字符串列表序列化方法,用逗号连接
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/12 10:02
 */
public class ListStringSerializer extends JsonSerializer<List<String>> {
    @Override
    public void serialize(List<String> ids, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(String.join(",",ids));
    }
}
