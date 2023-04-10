package com.gin.route.enums;

/**
 * 逻辑关系
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/3/25 17:46
 */
public enum Logic {
    /**
     * 有任意一个策略的计算结果为true时，返回true
     */
    any_true,
    /**
     * 有任意一个策略的计算结果为false时，返回false
     */
    any_false,
}
