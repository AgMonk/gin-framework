package route.annotation;


import route.entity.EleMenuItem;
import route.enums.Logic;
import route.strategy.AlwaysFalseStrategy;
import route.strategy.VisibleStrategy;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 路由, 在Controller类上标注，表示这是一个路由导航项 对应一个 {@link  EleMenuItem} 组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 17:42
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MenuItem {
    /**
     * 菜单名称
     */
    String menuName() default "index";

    /**
     * 排序序号,将按此字段降序排列
     */
    int order() default 0;

    /**
     * 组件的route属性, 留空时从 Controller 的 RequestMapping 注解上获取
     */
    String route() default "";

    /**
     * 组件的title属性
     */
    String title();

    /**
     * 描述, 该路由内的功能
     */
    String description() default "";

    /**
     * 保存路径，决定SubMenu的层级和分组，只有最后一个成员可以是分组
     */
    MenuPath[] path() default {};

    /**
     * {@link MenuEntry} 有多项时互相的逻辑关系
     */
    Logic logic() default Logic.any_false;

    /**
     * 展示策略，决定 disabled字段值的策略，默认为全显示
     */
    Class<? extends VisibleStrategy> strategy() default AlwaysFalseStrategy.class;

}
