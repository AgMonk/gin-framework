package com.gin.security.dto.form;

import com.gin.security.Constant.Security;
import com.gin.security.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import com.gin.spring.validation.Phone;


/**
 * 注册表单
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/17 16:48
 */
@Schema(description = "注册表单")
@Getter
@Setter
public class RegForm {
    @Schema(description = "用户名,长度范围为 [6,20]")
    @NotNull
    @Length(min = 6, max = 20)
    String username;
    @Schema(description = "密码,长度范围为 [" + Security.PASSWORD_MIN_LENGTH + "," + Security.PASSWORD_MAX_LENGTH + "]")
    @NotNull
    @Password
    String password;

    // 个人信息部分

    @NotNull
    @Schema(description = "昵称,长度范围为 [3,10]")
    @Length(min = 3, max = 10)
    String nickname;
    @Schema(description = "联系电话,支持的格式为:11位手机/7位固话/带区号固话(可-号分隔)")
    @Phone(nullable = true)
    String phone;
    @Schema(description = "生日(UNIX秒)")
    Long birthday;
}
