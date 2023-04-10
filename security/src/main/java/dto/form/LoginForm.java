package dto.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author bx002
 */
@Schema(description = "登录表单")
@Getter
@Setter
public class LoginForm {
    @Schema(description = "用户名")
    @NotNull
    String username;
    @Schema(description = "密码")
    @NotNull
    String password;
    @Schema(description = "验证码")
    @NotNull
    String vc;
    @Schema(description = "记住我( true / yes / on / 1 表示开启) ")
    @NotNull
    String rememberMe;
}