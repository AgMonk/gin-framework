package annotation;


import enums.OperationType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要记录操作日志
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/2/18 14:28
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OpLog {
    /**
     * 主实体类型
     */
    Class<?> mainClass() default Object.class;

    /**
     * 主实体ID  Spring-EL 表达式
     */
    String[] mainId() default {"#id", "#result?.data?.id", "#form?.id"};

    /**
     * 副实体类型
     */
    Class<?> subClass() default Object.class;

    /**
     * 副实体ID  Spring-EL 表达式
     */
    String[] subId() default {"#id", "#result?.data?.id", "#form?.id"};

    /**
     * 操作类型
     */
    OperationType type();

    /**
     * 执行方法之前计算的 Spring-EL 表达式
     */
    String[] preExp() default {};

    /**
     * 执行方法之后计算的 Spring-EL 表达式
     */
    String[] sufExp() default {"#result?.data"};

    /**
     * 是否记录请求参数
     */
    boolean requestParam() default true;

    /**
     * 是否记录返回结果
     */
    boolean responseResult() default true;
}
