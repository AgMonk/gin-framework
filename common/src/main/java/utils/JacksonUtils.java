package utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * json工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 15:44
 */
public class JacksonUtils {
    public final static ObjectMapper MAPPER = getMapper();

    public static ObjectMapper getMapper() {
        return new Jackson2ObjectMapperBuilder()
                .serializationInclusion(JsonInclude.Include.NON_NULL)
                .featuresToEnable(
                        //美化输出
                        SerializationFeature.INDENT_OUTPUT,
                        //反序列化时 空串识别为 null
                        DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT
                ).featuresToDisable(
                        // 反序列化时,遇到未知属性会不会报错
                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
                ).modules(
                        //支持 ZonedDateTime
                        new JavaTimeModule()
                )

                .build();
    }

    public static void printPretty(Object obj) {
        try {
            System.out.println(MAPPER.writeValueAsString(obj));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}   
