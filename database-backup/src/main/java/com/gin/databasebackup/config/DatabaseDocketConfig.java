package com.gin.databasebackup.config;

import com.gin.databasebackup.controller.AbstractDatabaseController;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 数据库模块接口文档
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 11:31
 */
@Configuration
public class DatabaseDocketConfig {
    @Bean
    public GroupedOpenApi databaseApi() {
        return GroupedOpenApi.builder()
                .group("数据库")
                .pathsToMatch(AbstractDatabaseController.API_PREFIX + "/**")
                .build();
    }

}   
