package properties;

import lombok.Getter;
import lombok.Setter;
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
@ConfigurationProperties(prefix = "system.user")
@Getter
@Setter
public class UserProperties implements Serializable {
    /**
     * 是否开放注册
     */
    boolean regEnable = true;
}
