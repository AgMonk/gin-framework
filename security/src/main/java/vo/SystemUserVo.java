package vo;


import base.BaseVo;
import entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "用户账号信息")
public class SystemUserVo extends BaseVo {
    @Schema(description = "用户名")
    String username;
    @Schema(description = "账号未过期")
    Boolean accountNonExpired;
    @Schema(description = "账号未锁定")
    Boolean accountNonLocked;
    @Schema(description = "密码未过期")
    Boolean credentialsNonExpired;
    @Schema(description = "是否可用")
    Boolean enabled;

    public SystemUserVo(SystemUser systemUser) {
        BeanUtils.copyProperties(systemUser, this);
    }
}