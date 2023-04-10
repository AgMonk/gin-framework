package com.gin.databasebackup.properties;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.util.List;

import static com.gin.common.constant.Messages.OFF;
import static com.gin.common.constant.Messages.ON;

/**
 * 数据库相关配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 17:03
 */
@Configuration
@ConfigurationProperties(prefix = "system.database")
@Getter
@Setter
@Slf4j
public class DatabaseProperties implements Serializable {

    /**
     * 自动备份开关
     */
    boolean autoBackup;
    /**
     * 保留的最大镜像数量, 每天凌晨清理,保留最新的。0或负数表示不限制
     */
    int maxBackup;
    /**
     * mysql client 安装包地址(避免修改)
     */
    List<String> mysqlClient;

    @PostConstruct
    public void onCreated(){
        log.info("自动备份 [{}], 最大镜像数 {} 个", autoBackup? ON : OFF, maxBackup);
    }
}
