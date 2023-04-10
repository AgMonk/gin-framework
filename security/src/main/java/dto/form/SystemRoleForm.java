package dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import entity.SystemRole;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "角色表单")
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SystemRoleForm {
    @Schema(description = "名称")
    @NotNull
    String name;
    @Schema(description = "中文名称")
    @NotNull
    String nameZh;
    @Schema(description = "描述")
    String description;
    @Schema(description = "备注")
    String remark;

    public SystemRole build() {
        final SystemRole systemRole = new SystemRole();
        BeanUtils.copyProperties(this, systemRole);
        return systemRole;
    }
}