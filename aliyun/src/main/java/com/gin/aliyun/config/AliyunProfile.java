package com.gin.aliyun.config;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.profile.DefaultProfile;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云配置
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.profile")
@Getter
@Setter
public class AliyunProfile {
    /**
     * 地区ID
     */
    String regionId;
    /**
     * key
     */
    String accessKey;
    /**
     * secret
     */
    String accessKeySecret;

    /**
     * 生成默认Profile
     *
     * @return DefaultProfile
     */
    public DefaultProfile obtainDefaultProfile() {
        return DefaultProfile.getProfile(regionId, accessKey, accessKeySecret);
    }

    /**
     * 生成Client
     *
     * @return IAcsClient
     */
    public IAcsClient obtainClient() {
        return new DefaultAcsClient(obtainDefaultProfile());
    }
}