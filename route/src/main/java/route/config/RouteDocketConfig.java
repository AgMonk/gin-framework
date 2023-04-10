package route.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import route.controller.RouteMenuController;

/**
 * 路由接口文档
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 15:28
 */
@Configuration
public class RouteDocketConfig {
    @Bean
    public GroupedOpenApi routeApi() {
        return GroupedOpenApi.builder()
                .group("路由导航菜单")
                .pathsToMatch(RouteMenuController.GROUP_NAME + "/**")
                .build();
    }
}   
