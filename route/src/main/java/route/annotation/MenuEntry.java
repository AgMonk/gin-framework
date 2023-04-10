package route.annotation;


import route.strategy.AlwaysFalseStrategy;
import route.strategy.VisibleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 导航项入口, 在 {@link MenuItem} 的方法上标记，表示该方法需要参与对 disabled 字段的计算; 不使用本注解时, 该字段永远为 false
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 10:48
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MenuEntry {
    /**
     * 展示策略，决定 disabled字段值的策略, 优先级高于 {@link MenuItem} 的同名字段
     */
    Class<? extends VisibleStrategy> strategy() default AlwaysFalseStrategy.class;
}
