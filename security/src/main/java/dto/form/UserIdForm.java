package dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import service.impl.SystemUserServiceImpl;
import validation.EntityId;

/**
 * 带有用户id的表单
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 15:58
 */
@Schema(description = "带有用户id的表单")
@Getter
@Setter
public class UserIdForm {
    @EntityId(service = SystemUserServiceImpl.class)
    @NotNull
    @Schema(description = "用户id")
    Long userId;

}   
