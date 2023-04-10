package com.gin.operationlog.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 17:03
 */
@Configuration
@ConfigurationProperties(prefix = "system.log")
@Getter
@Setter
public class OperationLogProperties implements Serializable {
    /**
     * 日志保留天数, 超过时间的将被存入旧日志表
     */
    int days = 1;
    /**
     * todo
     * 旧日志表保留天数, 超过时间的将被删除
     */
    int daysOld = 180;
}
