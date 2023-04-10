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
@ConfigurationProperties(prefix = "system.file")
@Getter
@Setter
@Slf4j
public class FileProperties implements Serializable {
    /**
     * 文件根目录,本系统管理的文件将全部放在该目录下
     */
    String homePath;

    @PostConstruct
    public void onCreated() {
        log.info("文件根目录: {}",homePath);
    }
}
