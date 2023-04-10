package com.gin.common.utils;

import jakarta.validation.constraints.NotNull;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 集合工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/24 11:47
 */
public class CollUtils {

    /**
     * 交集的补集（析取）
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 交集
     */
    public static <T> List<T> disjunction(Collection<T> colA, Collection<T> colB) {
        final List<T> union = union(colA, colB);
        final List<T> intersection = intersection(colA, colB);

        return subtract(union, intersection);
    }

    /**
     * 列表查重
     * @param list 列表
     * @return 重复出现的元素
     */
    public static <T> Set<T> distinct(@NotNull List<T> list) {
        final HashSet<T> set = new HashSet<>();
        final HashSet<T> duplicate = new HashSet<>();
        list.forEach(item -> {
            if (!set.contains(item)) {
                // set中不存在的，放入set
                set.add(item);
            } else {
                // set中存在的，放入重复 set
                duplicate.add(item);
            }
        });
        return duplicate;
    }

    /**
     * 求交集
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 交集
     */
    public static <T> List<T> intersection(Collection<T> colA, Collection<T> colB) {
        assert colA != null;
        assert colB != null;
        return colA.stream().filter(colB::contains).collect(Collectors.toList());
    }

    /**
     * 将 KeyList中的对象按照match方法与ValueList中的对象进行匹配，将匹配上的对象按照KeyList的顺序输出
     */
    public static <K, V> List<V> pick(List<K> keyList, List<V> valueList, Matcher<K, V> matcher) {
        final ArrayList<V> res = new ArrayList<>();
        keyList.forEach(k -> valueList.stream().filter(v -> matcher.match(k, v)).findFirst().ifPresent(res::add));
        return res;
    }

    /**
     * 差集（扣除）
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 差集（扣除）
     */
    public static <T> List<T> subtract(Collection<T> colA, Collection<T> colB) {
        return colA.stream().filter(o -> !colB.contains(o)).collect(Collectors.toList());
    }

    /**
     * 求并集
     * @param colA 集合A
     * @param colB 集合B
     * @param <T>  集合成员类型
     * @return 并集
     */
    public static <T> List<T> union(Collection<T> colA, Collection<T> colB) {
        final HashSet<T> set = new HashSet<>();
        set.addAll(colA);
        set.addAll(colB);
        return new ArrayList<>(set);
    }


    public interface Matcher<K, V> {
        boolean match(K key, V value);
    }
}   
