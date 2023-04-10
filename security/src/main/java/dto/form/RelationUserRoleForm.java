package dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import entity.RelationUserRole;
import org.springframework.beans.BeanUtils;
import org.springframework.validation.annotation.Validated;

/**
 * @author bx002
 */
@Getter
@Setter
@Schema(description = "用户角色表单")
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class RelationUserRoleForm {
    @Schema(description = "角色id")
    @NotNull
    Long roleId;

    @Schema(description = "过期时间(UNIX秒) 不小于0 0表示不过期")
    @Min(0L)
    Long timeExpire;

    public RelationUserRole build(long userId) {
        final RelationUserRole userRole = new RelationUserRole();
        BeanUtils.copyProperties(this, userRole);
        userRole.setUserId(userId);
        return userRole;
    }
}