package com.gin.route.base;

import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * 有子项目的组件
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 16:37
 */
public interface HasChildren<T> {
    /**
     * 子项目列表getter
     * @return 子项目列表getter
     */
    List<T> getChildren();

    /**
     * 子项目列表setter
     * @param list 列表
     */
    void setChildren(List<T> list);

    /**
     * 排序子项目
     */
    void sortChildren();

    /**
     * 添加子项目
     * @param t 子项目
     */
    default void addChildren(T t) {
        final List<T> list = getChildren() != null ? getChildren() : new ArrayList<>();
        list.add(t);
        setChildren(list);
    }

    /**
     * 从 Children 中查找第一个符合条件的对象，如果没有则返回默认值，并把返回值加入children
     * @param condition    条件
     * @param defaultValue 默认值
     * @return 子项目
     */
    default T findOrDefault(Function<T, Boolean> condition, @NotNull T defaultValue) {
        final List<T> children = getChildren();
        if (children == null || children.size() == 0) {
            return defaultValue;
        }
        final T t = children.stream().filter(condition::apply).findFirst().orElse(null);
        if (t == null) {
            children.add(defaultValue);
            return defaultValue;
        }
        return t;
    }
}   
