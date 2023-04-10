package config;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 备份配置
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 12:43
 */
@Configuration
public class DatabaseBackupConfig {

    @Bean
    public config.DatabaseConConfig databaseConConfig(DataSourceProperties dataSourceProperties){
        return new config.DatabaseConConfig(dataSourceProperties.getUrl());
    }
}   
