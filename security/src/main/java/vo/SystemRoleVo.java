package vo;


import base.BaseVo;
import entity.SystemRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "角色响应对象")
public class SystemRoleVo extends BaseVo {
    @Schema(description = "名称")
    String name;
    @Schema(description = "中文名称")
    String nameZh;
    @Schema(description = "描述")
    String description;
    @Schema(description = "备注")
    String remark;
    @Schema(description = "修改时间(UNIX秒)")
    Long timeUpdate;

    public SystemRoleVo(SystemRole systemRole) {
        BeanUtils.copyProperties(systemRole, this);
    }
}