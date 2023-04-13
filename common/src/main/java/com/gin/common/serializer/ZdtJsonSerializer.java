package com.gin.common.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gin.common.utils.TimeUtils;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * 日期序列化方式
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/12 11:37
 */
public class ZdtJsonSerializer extends JsonSerializer<ZonedDateTime> {

    @Override
    public void serialize(ZonedDateTime zonedDateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeString(TimeUtils.format(zonedDateTime.withZoneSameInstant(ZoneId.systemDefault())));
    }
}
