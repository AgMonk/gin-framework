package old.user.vo;

import com.gin.springboot3template.sys.base.BaseVo;
import com.gin.springboot3template.user.entity.RelationRolePermission;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 11:52
 */
@Getter
@Setter
@Schema(description = "角色权限响应对象")
@NoArgsConstructor
@AllArgsConstructor
public class SystemRolePermissionVo extends BaseVo {
    @Schema(description = "角色id")
    Long roleId;
    @Schema(description = "权限id")
    List<Long> permIds;

    public static SystemRolePermissionVo of(Long roleId, List<RelationRolePermission> rolePermissions) {
        return new SystemRolePermissionVo(roleId, rolePermissions.stream().map(RelationRolePermission::getPermissionId).collect(Collectors.toList()));
    }
}