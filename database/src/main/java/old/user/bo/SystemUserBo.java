package old.user.bo;

import com.gin.springboot3template.sys.base.BaseBo;
import com.gin.springboot3template.user.entity.SystemUser;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "用户及其持有的角色&权限")
public class SystemUserBo extends BaseBo {
    @Schema(description = "角色&权限")
    List<RelationUserRoleBo> roles;
    @Schema(description = "账号未过期")
    private boolean accountNonExpired;
    @Schema(description = "账号未锁定")
    private boolean accountNonLocked;
    @Schema(description = "密码未过期")
    private boolean credentialsNonExpired;
    @Schema(description = "是否可用")
    private boolean enabled;
    @Schema(description = "用户名")
    private String username;

    public SystemUserBo(SystemUser systemUser) {
        BeanUtils.copyProperties(systemUser, this);
    }
}