package com.gin.security.validation;


import com.gin.spring.validation.ValidatorUtils;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 密码校验器
 * @author bx002
 */
public class PasswordValidator implements ConstraintValidator<Password, String> {
    /**
     * 1.必须包含数字、大小写字母
     * 2.密码位数在8-16位
     */
    public static final Pattern PATTERN = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{8,16}$");
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
        final Matcher matcher = PATTERN.matcher(s);
        final boolean matches = matcher.matches();
        if (!matches) {
            ValidatorUtils.changeMessage(context, prefix + "密码必须包含：数字、大写字母、小写字母，位数为8-16位");
        }
        return matches;
    }
}
