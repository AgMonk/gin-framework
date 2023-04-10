package com.gin.database.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 服务状态
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 16:19
 */
@Getter
@AllArgsConstructor
public enum ServiceStatus {
    /**
     * 不可用
     */
    disable("不可用"),
    /**
     * 正在准备
     */
    preparing("正在准备.."),
    /**
     * 正常
     */
    enable("正常"),
    ;
    final String zh;
}
