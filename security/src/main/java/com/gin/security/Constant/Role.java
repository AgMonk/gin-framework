package com.gin.security.Constant;

import java.util.List;

/**
     * 角色相关
 * @author bx002
 */
    public class Role {
        /**
         * 预设角色 超管
         */
        public static final String ADMIN = "admin";
        /**
         * 预设角色 角色管理员
         */
        public static final String ROLE_MANAGER = "roleManager";
        /**
         * 预设角色集合
         */
        public static final List<String> DEFAULT_ROLES = List.of(ADMIN, ROLE_MANAGER);
    }