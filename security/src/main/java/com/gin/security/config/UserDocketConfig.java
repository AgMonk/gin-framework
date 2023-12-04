package com.gin.security.config;

import com.gin.security.wechat.WechatController;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bx002
 */
@Configuration
public class UserDocketConfig {
    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户_角色_权限")
                .pathsToMatch("/sys/**")
                .build();
    }
    @Bean
    public GroupedOpenApi wechatApi() {
        return GroupedOpenApi.builder()
                .group(WechatController.GROUP_NAME)
                .pathsToMatch(WechatController.API_PREFIX + "/**")
                .build();
    }
}
