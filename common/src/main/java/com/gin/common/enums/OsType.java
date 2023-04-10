package com.gin.common.enums;

/**
 * 操作系统类型
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 14:41
 */
public enum OsType {
    /**
     * WINDOWS
     */
    WINDOWS,
    /**
     * LINUX
     */
    LINUX,
    /**
     * MAC
     */
    MAC,
    /**
     * 未知
     */
    UNKNOWN,
    ;

    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static OsType find() {
        final String name = System.getProperty("os.name");
        if (name == null) {
            return UNKNOWN;
        }
        if (name.startsWith("Mac")) {
            return MAC;
        }
        if (name.startsWith("Windows")) {
            return WINDOWS;
        }
        if (name.startsWith("Linux")) {
            return LINUX;
        }
        return UNKNOWN;
    }
}
