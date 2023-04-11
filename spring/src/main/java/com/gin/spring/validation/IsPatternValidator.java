package com.gin.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.regex.Pattern;

/**
 * 是否为合法正则表达式校验器
 * @author bx002
 */
public class IsPatternValidator implements ConstraintValidator<IsPattern, String> {
    private String message;
    private boolean nullable;
    private String prefix;

    @Override
    public void initialize(IsPattern constraintAnnotation) {
        this.nullable = constraintAnnotation.nullable();
        this.prefix = constraintAnnotation.prefix();
        this.message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (nullable && ObjectUtils.isEmpty(value)) {
            return true;
        }
        try {
            Pattern.compile(value);
        } catch (Exception e) {
            ValidatorUtils.changeMessage(context, this.prefix + this.message);
            return false;
        }
        return true;
    }
}
