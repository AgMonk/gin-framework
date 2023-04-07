package old.route.annotation;

/**
 * 路由路径,用于创建SubMenu和MenuItemGroup
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 10:15
 */
public @interface MenuPath {
    /**
     * 是否为 menu_item_group ,group 只能放在 path 的最后一个
     */
    boolean isGroup() default false;

    /**
     * 标题
     */
    String title();

    /**
     * order
     */
    int order();
}
