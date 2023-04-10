package old.user.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 16:11
 */
@Schema(description = "删除用户角色表单")
@Getter
@Setter
public class UserRoleDelForm extends UserIdForm {
    @Schema(description = "角色id")
    @NotEmpty
    List<Long> roleId;
}   
