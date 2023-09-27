package com.gin.operationlog.service;

import java.util.Collection;
import java.util.HashMap;

/**
 * 用户信息服务
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/4/10 14:33
 */
public interface UserInfoService {
    /**
     * 返回当前登陆用户的用户id
     * @return 用户id
     */
    Long getCurrentUserId();

    /**
     * 查询 userId 到昵称的映射
     * @param userId 用户id
     * @return dic
     */
    HashMap<Long, String> getIdNameMap(Collection<Long> userId);

}
