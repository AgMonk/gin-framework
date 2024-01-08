package com.gin.spring.config;

import com.gin.spring.properties.FileProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

/**
 * 配置静态资源映射
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class MyStaticConfig implements WebMvcConfigurer {
    /**
     * 静态资源映射路径前缀
     */
    public static final String STATIC_PATH_PREFIX = "/files";
    private final FileProperties systemProperties;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        final String location = "file://" + systemProperties.getHomePath() + "/";
        final String path = STATIC_PATH_PREFIX + "/**";
        log.info("配置静态资源映射: {} -> {}", path, location);
        registry
                .addResourceHandler(path)
                .addResourceLocations(location)
                .setCacheControl(CacheControl.maxAge(10, TimeUnit.MINUTES))
        ;
    }

}
