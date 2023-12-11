package com.gin.security.service;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.database.service.MyService;
import com.gin.security.entity.SystemUserPhone;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 15:44
 **/
public interface SystemUserPhoneService extends MyService<SystemUserPhone> {
    /**
     * 发送验证码
     *
     * @param phoneNumber 手机号
     */
    void sendCode(String phoneNumber) throws ClientException, JsonProcessingException;

    /**
     * 校验验证码
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     */
    void checkCode(String phoneNumber, String code);

    /**
     * 解绑
     *
     * @param userId 用户id
     */
    void unbindByUserId(long userId);

    /**
     * 根据用户id查询绑定手机
     *
     * @param userId 用户id
     * @return 绑定手机
     */
    SystemUserPhone getByUserId(long userId);

    /**
     * 根据手机查询用户
     *
     * @param phoneNumber 手机号
     * @return 绑定手机
     */
    SystemUserPhone getByPhoneNumber(String phoneNumber);
}