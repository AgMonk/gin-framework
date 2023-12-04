package com.gin.security.wechat;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.net.URL;

/**
 * 小程序配置类
 */
@Configuration
@ConfigurationProperties(prefix = "wechat")
@Getter
@Setter
public class WechatConfig {
    public static final String URL_TEMPLATE = "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code";
    String appId;
    String appSecret;

    /**
     * 生成请求地址
     * @param code code
     * @return 请求地址
     */
    public String obtainUrl(String code){
        return String.format(URL_TEMPLATE,appId,appSecret,code);
    }
}