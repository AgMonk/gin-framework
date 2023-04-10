package com.gin.route.service;

import com.gin.route.strategy.AlwaysFalseStrategy;
import com.gin.route.strategy.VisibleStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import com.gin.route.annotation.MenuEntry;
import com.gin.route.annotation.MenuItem;
import com.gin.route.annotation.MenuPath;
import com.gin.route.base.EleMenuComponent;
import com.gin.route.base.HasChildren;
import com.gin.route.entity.EleMenuItem;
import com.gin.route.entity.EleMenuItemGroup;
import com.gin.route.entity.EleSubMenu;
import com.gin.common.utils.SpringContextUtils;
import com.gin.common.utils.reflect.ReflectUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 路由导航菜单服务, 扫描路由导航, 根据给出的菜单名称和当前用户的权限返回路由导航菜单
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/27 10:31
 */
@Service
@RequiredArgsConstructor
public class MenuService {

    private static Collection<Object> getControllers() {
        return SpringContextUtils.getContext().getBeansWithAnnotation(MenuItem.class).values();
    }

    /**
     * 根据菜单名查询路由导航项
     * @param menuName 菜单名
     * @return 路由导航项
     */
    public List<EleMenuComponent> listItemByMenuName(String menuName) {
        // 含有 MenuItem 注解的 controller 的class
        final List<? extends Class<?>> controllerClasses = getControllers().stream()
                .map(ReflectUtils::getControllerClass)
                .filter(Objects::nonNull)
                .filter(clazz -> clazz.getAnnotation(MenuItem.class).menuName().equals(menuName))
                .toList();
        // 根列节点
        final EleSubMenu root = new EleSubMenu();
        // 返回的路由导航列表

        for (Class<?> clazz : controllerClasses) {
            // controller 上的注解
            final MenuItem menuItem = clazz.getAnnotation(MenuItem.class);
            // controller 中标记了 MenuEntry注解的方法
            final List<Method> methods = Arrays.stream(clazz.getDeclaredMethods()).filter(method -> method.getAnnotation(MenuEntry.class) != null).toList();

            final EleMenuItem eleMenuItem = new EleMenuItem(menuItem);
            // controller 上的url前缀
            final String ctlPath = ReflectUtils.getApiPath(clazz).stream().findFirst().orElse("");
            // 组件的route属性, 留空时从 Controller 的 RequestMapping 注解上获取
            if (ObjectUtils.isEmpty(menuItem.route())) {
                eleMenuItem.setRoute(ctlPath);
            }

            //如果 使用了 MenuEntry 注解，调用策略计算 disabled 字段
            if (!CollectionUtils.isEmpty(methods)) {

                // 计算结果
                final List<Boolean> results = methods.stream().map(method -> {
                    final MenuEntry menuEntry = method.getAnnotation(MenuEntry.class);
                    // 决定使用的策略类型  MenuEntry 优先于 MenuItem
                    final Class<? extends VisibleStrategy> strategyClass =
                            menuEntry.strategy() != AlwaysFalseStrategy.class ? menuEntry.strategy() : menuItem.strategy();
                    // 策略
                    final VisibleStrategy strategy = SpringContextUtils.getContext().getBean(strategyClass);

                    final String apiPath = ReflectUtils.getApiPath(method).get(0);
                    // 请求地址
                    final String requestUrl = (ctlPath + "/" + apiPath).replaceAll("//", "/");

                    return strategy.isDisable(menuItem, method, requestUrl);
                }).distinct().toList();

                switch (menuItem.logic()) {
                    case any_true -> eleMenuItem.setDisabled(results.stream().anyMatch(i -> i));
                    case any_false -> eleMenuItem.setDisabled(results.stream().allMatch(i -> i));
                    default -> throw new IllegalStateException("Unexpected value: " + menuItem.logic());
                }
            }

            // 将路由导航项放到指定路径 和分组中
            final MenuPath[] paths = menuItem.path();
            if (paths != null && paths.length > 0) {
                // 当前节点
                HasChildren<EleMenuComponent> node = root;
                for (int i = 0; i < paths.length; i++) {
                    final MenuPath menuPath = paths[i];
                    if (menuPath.isGroup()) {
                        // 如果是分组，创建分组，跳出循环
                        node = (EleMenuItemGroup) node.findOrDefault(
                                g -> g instanceof EleMenuItemGroup && g.getTitle().equals(menuPath.title()),
                                new EleMenuItemGroup(menuPath)
                        );
                        break;
                    } else {
                        // 不是分组，创建路径，把当前节点指向新路径
                        node = (EleSubMenu) node.findOrDefault(
                                sub -> sub instanceof EleSubMenu && sub.getTitle().equals(menuPath.title()),
                                new EleSubMenu(menuPath)
                        );
                    }
                }
                // 将item放入当前路径
                node.addChildren(eleMenuItem);
            } else {
                root.addChildren(eleMenuItem);
            }

            // 排序
            root.sortChildren();
        }
        return root.getChildren();
    }

    /**
     * 返回所有菜单名称
     * @return 菜单名称
     */
    public List<String> listMenuNames() {
        return getControllers().stream()
                .map(ReflectUtils::getControllerClass)
                .filter(Objects::nonNull)
                .map(clazz -> clazz.getAnnotation(MenuItem.class))
                .map(MenuItem::menuName)
                .distinct()
                .toList();
    }

}   
