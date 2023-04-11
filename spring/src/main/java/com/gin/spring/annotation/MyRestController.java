package com.gin.spring.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.*;


/**
 * 统一Controller注解
 * @author bx002
 * @version 2022/4/14 - 12:08
 */

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RestController
@RequestMapping
@Validated
@Transactional
public @interface MyRestController {
    @AliasFor(annotation = RequestMapping.class)
    String[] value() default {};

    @AliasFor(annotation = RequestMapping.class)
    String[] path() default {};

    @AliasFor(annotation = Transactional.class)
    Class<?> rollbackFor() default Exception.class;


}