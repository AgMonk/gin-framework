package config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共接口文档
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 11:32
 */
@Configuration
public class CommonDocketConfig {
    @Bean
    public GroupedOpenApi testApi() {
        return GroupedOpenApi.builder()
                .group("测试")
                .pathsToMatch("/test/**")
                .build();
    }

}   
