package com.gin.aliyun.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 验证码服务使用的签名和模板CODE
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.code")
@Getter
@Setter
public class AliyunCode {
    /**
     * 签名
     */
    String signName;
    /**
     * 模板code
     */
    String templateCode;
}