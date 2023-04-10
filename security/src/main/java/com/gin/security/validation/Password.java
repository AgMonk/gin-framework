package com.gin.security.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 密码验证注解
 * @author bx002
 */
@Documented
@Target({PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "密码不合法";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String prefix() default "";

    boolean nullable() default false;
}
