package com.gin.spring.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

import java.util.Collection;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * 电话校验器
 *
 * @author bx002
 */
public class PhoneValidator implements ConstraintValidator<Phone, Object> {
    public static final String MOBILE_REGEX = "^1[3-9][0-9]{9}$";
    public static final Pattern MOBILE_PATTERN = Pattern.compile(MOBILE_REGEX);
    public static final Pattern TEL_PATTERN = Pattern.compile("^0\\d{2,3}-?\\d{7}$");
    public static final Pattern TEL_PATTERN_2 = Pattern.compile("^\\d{7}$");

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        // 如果注解值为合集，将合集元素 toString() 进行判断

        if (value instanceof Collection<?> collection) {
            // 不匹配的电话号码
            final Set<String> set = collection.stream().filter(i -> !isPhone(i)).map(String::valueOf).collect(Collectors.toSet());
            if (ObjectUtils.isEmpty(set)) {
                return true;
            }
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("电话号码不合法: " + String.join(", ", set)).addConstraintViolation();
            return false;
        }

        // 非合集，直接对注解值 toString() 进行判断

        final boolean b = isPhone(value);
        if (!b) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("电话号码不合法: " + value).addConstraintViolation();
        }
        return b;
    }

    public static boolean isMobilePhone(Object o) {
        return MOBILE_PATTERN.matcher(String.valueOf(o)).find();
    }

    public static boolean isTelPhone(Object o) {
        return TEL_PATTERN.matcher(String.valueOf(o)).find() || TEL_PATTERN_2.matcher(String.valueOf(o)).find();
    }

    public static boolean isPhone(Object o) {
        return isMobilePhone(o) || isTelPhone(o);
    }
}
