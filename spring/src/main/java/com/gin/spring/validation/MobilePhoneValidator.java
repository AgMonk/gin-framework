package com.gin.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2024/1/3 16:15
 **/
public class MobilePhoneValidator implements ConstraintValidator<MobilePhone, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        // 如果注解值为合集，将合集元素 toString() 进行判断

        if (value instanceof Collection<?> collection) {
            // 不匹配的电话号码
            final Set<String> set = collection.stream().filter(i -> !PhoneValidator.isMobilePhone(i)).map(String::valueOf).collect(Collectors.toSet());
            if (ObjectUtils.isEmpty(set)) {
                return true;
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("手机号不合法: " + String.join(", ", set)).addConstraintViolation();
            return false;
        }

        // 非合集，直接对注解值 toString() 进行判断

        final boolean b = PhoneValidator.isMobilePhone(value);
        if (!b) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("手机号不合法: " + value).addConstraintViolation();
        }
        return b;
    }

}
