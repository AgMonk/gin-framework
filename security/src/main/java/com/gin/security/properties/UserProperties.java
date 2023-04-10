package com.gin.security.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

import static com.gin.common.constant.Messages.OFF;
import static com.gin.common.constant.Messages.ON;

/**
 * 系统配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 17:11
 */
@Configuration
@ConfigurationProperties(prefix = "system.user")
@Getter
@Setter
@Slf4j
public class UserProperties implements Serializable {
    /**
     * 是否开放注册
     */
    boolean regEnable = true;

    @PostConstruct
    public void onCreated() {
        log.info("注册功能: {}", regEnable ? ON : OFF);
    }
}
