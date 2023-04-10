package com.gin.security.validation;


import com.gin.security.Constant.Security;
import com.gin.common.validation.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * 密码校验器
 * @author bx002
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
    String prefix;
    private boolean nullable;

    @Override
    public void initialize(Password constraint) {
        this.nullable = constraint.nullable();
        this.prefix = constraint.prefix();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext context) {
        if (s == null || "".equals(s)) {
            if (nullable) {
                return true;
            }
            ValidatorUtils.    changeMessage(context, prefix + "密码不允许为空");
            return false;
        }
        if (s.length() < Security.PASSWORD_MIN_LENGTH || s.length() > Security.PASSWORD_MAX_LENGTH) {
            ValidatorUtils.  changeMessage(context,
                          prefix + String.format("密码长度应介于 [%d,%d]",
                                                 Security.PASSWORD_MIN_LENGTH,
                                                 Security.PASSWORD_MAX_LENGTH));
            return false;
        }

        return true;
    }
}
