package old.sys.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author bx002
 */
@Configuration
public class DocketConfig {

    //todo 拆解
    @Bean
    public GroupedOpenApi databaseApi() {
        return GroupedOpenApi.builder()
                .group("数据库")
                .pathsToMatch("/database/**")
                .build();
    }

    @Bean
    public GroupedOpenApi routeApi() {
        return GroupedOpenApi.builder()
                .group("路由导航菜单")
                .pathsToMatch("/route/**")
                .build();
    }

    @Bean
    public GroupedOpenApi testApi() {
        return GroupedOpenApi.builder()
                .group("测试")
                .pathsToMatch("/test/**")
                .build();
    }

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder()
                .group("用户_角色_权限")
                .pathsToMatch("/sys/**")
                .build();
    }

}
