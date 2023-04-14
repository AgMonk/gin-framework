package com.gin.common.utils;

import java.util.Arrays;
import java.util.Objects;

/**
 * 对象工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/14 17:25
 */
public class ObjUtils {
    /**
     * 取数组中第一个非null值
     * @param t 对象
     * @return 第一个非null
     */
    public static <T> T findFirstNotNull(T... t){
       return Arrays.stream(t).filter(Objects::nonNull).findFirst().orElse(null);
    }
}   
