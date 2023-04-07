package old.user.bo;

import com.gin.springboot3template.sys.base.BaseBo;
import com.gin.springboot3template.user.entity.RelationUserRole;
import com.gin.springboot3template.user.entity.SystemPermission;
import com.gin.springboot3template.user.entity.SystemRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.util.List;

@Getter
@Setter
@Schema(description = "角色及其持有的权限")
public class RelationUserRoleBo extends BaseBo {
    @Schema(description = "用户id")
    Long userId;
    @Schema(description = "角色id")
    Long roleId;
    @Schema(description = "修改时间(UNIX秒)")
    Long timeUpdate;
    @Schema(description = "过期时间(UNIX秒)")
    Long timeExpire;


    @Schema(description = "名称")
    String name;
    @Schema(description = "中文名称")
    String nameZh;
    @Schema(description = "描述")
    String description;
    @Schema(description = "备注")
    String remark;
    @Schema(description = "权限")
    List<SystemPermission> permissions;

    public RelationUserRoleBo(RelationUserRole userRole) {
        BeanUtils.copyProperties(userRole, this);
    }

    /**
     * 补充三个字段信息
     * @param systemRole 角色
     */
    public void with(SystemRole systemRole) {
        if (systemRole == null) {
            return;
        }
        this.name = systemRole.getName();
        this.nameZh = systemRole.getNameZh();
        this.remark = systemRole.getRemark();
        this.description = systemRole.getDescription();
    }
}