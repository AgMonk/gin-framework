package com.gin.security.dto.form;

import com.gin.security.Constant.Security;
import com.gin.security.validation.Password;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;


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
    @Schema(description = "新密码,长度范围为 [" + Security.PASSWORD_MIN_LENGTH + "," + Security.PASSWORD_MAX_LENGTH + "];不传将随机生成")
    @Password(nullable = true)
    String newPass;

}   
