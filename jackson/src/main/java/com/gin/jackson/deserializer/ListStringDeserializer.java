package com.gin.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 字符串列表反序列化
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/13 15:12
 */
public class ListStringDeserializer extends JsonDeserializer<List<String>> {
    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        return new ArrayList<>(Arrays.asList(p.getValueAsString().split(",")));
    }
}
