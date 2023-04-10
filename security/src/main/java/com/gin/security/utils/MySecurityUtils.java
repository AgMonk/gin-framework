package com.gin.security.utils;

import com.gin.security.bo.MyUserDetails;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/15 10:54
 */
public class MySecurityUtils {
    /**
     * 当前用户的认证/权限信息
     * @return 当前用户的认证/权限信息
     */
    public static MyUserDetails currentUserDetails() {
        return ((MyUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
    }
}   
