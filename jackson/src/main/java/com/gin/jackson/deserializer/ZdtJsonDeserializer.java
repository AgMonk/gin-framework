package com.gin.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.gin.jackson.utils.JacksonUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * ZonedDateTime 解析
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/8/18 17:32
 **/
public class ZdtJsonDeserializer extends JsonDeserializer<ZonedDateTime> {
    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        final String s = jsonParser.getValueAsString();
        final LocalDateTime parse = LocalDateTime.parse(s, JacksonUtils.FORMATTER);
        return ZonedDateTime.ofLocal(parse, ZoneId.systemDefault(), null);
    }
}
