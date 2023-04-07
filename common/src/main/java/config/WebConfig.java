package config;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import config.converter.BaseEnumConverterFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.format.FormatterRegistry;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import properties.WebProperties;

/**
 * 项目全局配置类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 16:30
 */
@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final WebProperties webProperties;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverterFactory(new BaseEnumConverterFactory());
    }

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder().serializationInclusion(JsonInclude.Include.NON_NULL).featuresToEnable(
                //美化输出
                SerializationFeature.INDENT_OUTPUT
                //反序列化时 空串识别为 null
                , DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT).featuresToDisable(
                // 反序列化时,遇到未知属性会不会报错
//                        DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
        ).modules(
                //支持 ZonedDateTime
                new JavaTimeModule());

        // 是否在遇到未知属性时报错
        if (webProperties.isFailOnUnknownProperties()) {
            builder.featuresToEnable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        } else {
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }

        return builder.build();
    }
}   
