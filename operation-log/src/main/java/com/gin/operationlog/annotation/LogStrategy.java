package annotation;


import enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 日志生成策略，根据实体类型和操作类型匹配策略
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LogStrategy {
    /**
     * 匹配的实体类型
     */
    Class<?> value() default Object.class;

    /**
     * 匹配的操作类型
     */
    OperationType type();
}
