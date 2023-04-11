package com.gin.spring.validation;


import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 校验器工具类
 * @author bx002
 */
public class ValidatorUtils {
    public static void changeMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    /**
     * 验证obj存在于list中
     * @param obj     obj
     * @param list    有效值列表
     * @param context context
     * @return boolean
     */
    public static boolean validInList(String obj, List<String> list, String notNullName, ConstraintValidatorContext context) {
        if (obj == null || "".equals(obj)) {
            changeMessage(context, notNullName + "不允许为空");
            return false;
        }
        boolean contains = list.contains(obj);
        if (!contains) {
            changeMessage(context, notNullName + "的有效值为 " + list.stream().collect(Collectors.joining(",", "[", "]")));
            return false;
        }
        return true;
    }
}
