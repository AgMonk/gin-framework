package com.gin.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.gin.jackson.utils.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 整数列表反序列化
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/13 15:12
 */
public class ListLongDeserializer extends JsonDeserializer<List<Long>> {
    @Override
    public List<Long> deserialize(JsonParser p, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final String value = p.getValueAsString();
        if (ObjectUtils.isEmpty(value)) {
            return new ArrayList<>();
        }
        return Arrays.stream(value.split(","))
                .filter(i->!ObjectUtils.isEmpty(i))
                .map(Long::parseLong).collect(Collectors.toList());
    }
}
