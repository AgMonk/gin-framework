package old.user.dto.form;

import com.gin.springboot3template.sys.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import static com.gin.springboot3template.sys.bo.Constant.Security.PASSWORD_MAX_LENGTH;
import static com.gin.springboot3template.sys.bo.Constant.Security.PASSWORD_MIN_LENGTH;

/**
 * 重置密码表单
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/19 16:00
 */
@Schema(description = "带有用户id的表单")
@Getter
@Setter
public class ResetPasswordForm extends UserIdForm {
    @Schema(description = "新密码,长度范围为 [" + PASSWORD_MIN_LENGTH + "," + PASSWORD_MAX_LENGTH + "];不传将随机生成")
    @Password(nullable = true)
    String newPass;

}   
