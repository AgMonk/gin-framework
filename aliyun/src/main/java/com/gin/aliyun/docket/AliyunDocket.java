package com.gin.aliyun.docket;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云接口
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 13:41
 **/
@Configuration
public class AliyunDocket {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = "/aliyun";

    @Bean
    public GroupedOpenApi AliyunApi() {
        return GroupedOpenApi.builder()
                .group("阿里云接口")
                .pathsToMatch(API_PREFIX + "/**")
                .build();
    }
}

