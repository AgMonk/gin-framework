package com.gin.common.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * 向响应头中注入Cache-Control字段
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/22 14:14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheMaxAge {
    /**
     * @return 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 时间长度
     */
    @AliasFor("maxAge")
    long value() default 30;

    /**
     * 时间长度
     */
    @AliasFor("value")
    long maxAge() default 30;
}
