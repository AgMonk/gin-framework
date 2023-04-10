package com.gin.common.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * 系统配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:11
 */
@Configuration
@ConfigurationProperties(prefix = "system.web")
@Getter
@Setter
@Slf4j
public class WebProperties implements Serializable {
    /**
     * 解析json时是否在遇到未知字段时报错
     */
    boolean failOnUnknownProperties = true;

    @PostConstruct
    public void onCreated() {
        log.info("是否允许传递未知字段: {}",!failOnUnknownProperties);
    }
}
