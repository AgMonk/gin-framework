package com.gin.common.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 电话验证注解
 * @author bx002
 */
@Documented
@Target({PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IsPatternValidator.class)
public @interface IsPattern {
    String message() default "正则表达式不合法";

    String value() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    boolean nullable() default false;

    String prefix() default "";

}
