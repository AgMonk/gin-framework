package dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import service.impl.SystemRoleServiceImpl;
import org.springframework.validation.annotation.Validated;
import validation.EntityId;

import java.util.List;

/**
 * 角色权限
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/20 10:20
 */
@Getter
@Setter
@Schema(description = "角色权限表单")
@Validated
@NoArgsConstructor
@AllArgsConstructor
public class SystemRolePermissionForm {
    @Schema(description = "角色id")
    @NotNull
    @EntityId(service = SystemRoleServiceImpl.class)
    Long roleId;
    @Schema(description = "权限id")
    @NotNull
    List<Long> permIds;

}   