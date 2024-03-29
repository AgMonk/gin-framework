package com.gin.spring.validation;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;

/**
 * 电话验证注解 可以适配 toString() 方法能返回一个电话号码的对象，或该类对象的集合
 *
 * @author bx002
 */
@Documented
@Target({PARAMETER, FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {
    String message() default "电话号码不合法,支持的格式为:11位手机/7位固话/带区号固话(可-号分隔)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
