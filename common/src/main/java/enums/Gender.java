package enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 性别
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/19 15:31
 */
@Getter
@AllArgsConstructor
public enum Gender {
    /**
     * 男
     */
    male("男"),
    /**
     * 女
     */
    female("女"),
    /**
     * 秘密
     */
    secret("秘密");

    @JsonValue
    final String zh;
}
