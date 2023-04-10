package com.gin.common.annotation;


import com.gin.common.advice.JsonResponseWrapper;

import java.lang.annotation.*;

/**
 * 是否禁用{@link JsonResponseWrapper}的自动包装
 * @author bx002
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RestWrapper {
    /**
     * 禁用包装
     * @return 是否禁用
     */
    boolean disable() default true;
}