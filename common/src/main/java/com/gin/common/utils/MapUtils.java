package com.gin.common.utils;

import com.gin.jackson.utils.ObjectUtils;

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
     * 将集合转换为Map
     * @param collection 集合
     * @param getKey     Map的Key
     * @param getValue   Map的Value
     * @return Map
     */
    public static <K, V, T> HashMap<K, V> coll2Map(Collection<T> collection, Function<T, K> getKey, Function<T, V> getValue) {
        if (ObjectUtils.isEmpty(collection)) {
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
