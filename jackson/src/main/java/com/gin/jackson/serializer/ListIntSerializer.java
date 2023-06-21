package com.gin.jackson.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 整数列表序列化方法,用逗号连接
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/12 10:02
 */
public class ListIntSerializer extends JsonSerializer<List<Integer>> {
    @Override
    public void serialize(List<Integer> ids, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(ids.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }
}
