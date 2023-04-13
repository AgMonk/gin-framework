package com.gin.common.utils;

import org.springframework.cglib.beans.BeanMap;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Function;

/**
 * Map工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 16:50
 */
public class MapUtils {
    /**
     * 创建单个成员的hashmap
     * @param key key
     * @param value value
     * @return map
     */
    public static <K,V> HashMap<K, V> singleEntry(K key,V value){
        final HashMap<K, V> map = new HashMap<>(1);
        map.put(key, value);
        return map;
    }
    /**
     * 将集合转换为Map , 以 id 字段为key
     * @param collection 集合
     * @return Map
     */
    public static <T> HashMap<String, T> coll2Map(Collection<T> collection) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(0);
        }
        final BeanMap beanMap = BeanMap.create(new ArrayList<>(collection).get(0));
        return coll2Map(collection, t -> {
            beanMap.setBean(t);
            return String.valueOf(beanMap.get("id"));
        });
    }

    /**
     * 将集合转换为Map
     * @param collection 集合
     * @param getKey     Map的Key
     * @return Map
     */
    public static <K, T> HashMap<K, T> coll2Map(Collection<T> collection, Function<T, K> getKey) {
        return coll2Map(collection, getKey, t -> t);
    }

    /**
     * 将集合转换为Map
     * @param collection 集合
     * @param getKey     Map的Key
     * @param getValue   Map的Value
     * @return Map
     */
    public static <K, V, T> HashMap<K, V> coll2Map(Collection<T> collection, Function<T, K> getKey, Function<T, V> getValue) {
        if (CollectionUtils.isEmpty(collection)) {
            return new HashMap<>(0);
        }
        final HashMap<K, V> map = new HashMap<>(collection.size());
        collection.forEach(item -> {
            final K key = getKey.apply(item);
            final V value = getValue.apply(item);
            map.put(key, value);
        });
        return map;
    }
}   
