package com.gin.security.service.impl;

import com.aliyuncs.exceptions.ClientException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.aliyun.service.AliyunCodeService;
import com.gin.security.bo.MyUserDetails;
import com.gin.security.dao.SystemUserPhoneDao;
import com.gin.security.entity.SystemUserPhone;
import com.gin.security.service.SystemUserPhoneService;
import com.gin.security.utils.MySecurityUtils;
import com.gin.spring.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 15:45
 **/
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SystemUserPhoneServiceImpl extends ServiceImpl<SystemUserPhoneDao, SystemUserPhone> implements SystemUserPhoneService {
    private final AliyunCodeService aliyunCodeService;
    private final RedisTemplate<String, String> stringTemplate;

    @Override
    public void sendCode(String phoneNumber) throws ClientException, JsonProcessingException {
        // 检查是否处于冷却期间
        final Long expire = stringTemplate.getExpire(obtainKey(phoneNumber));
        if (expire != null && expire > 0) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "你的操作太频繁了请稍候再试");
        }

        final Random random = new Random();
        final int number = random.nextInt(1000, 9999);
        aliyunCodeService.sendCode(phoneNumber, String.valueOf(number));
    }

    @Override
    public void checkCode(String phoneNumber, String code) {
        aliyunCodeService.checkCode(phoneNumber, code);

        // 保存绑定
        final MyUserDetails myUserDetails = MySecurityUtils.currentUserDetails();
        final SystemUserPhone po = new SystemUserPhone();
        po.setUserId(myUserDetails.getId());
        po.setPhone(phoneNumber);
        save(po);

        // 设置绑定冷却
        stringTemplate.opsForValue().set(obtainKey(phoneNumber), "1", 1, TimeUnit.DAYS);
    }

    @Override
    public void unbindByUserId(long userId) {
        final QueryWrapper<SystemUserPhone> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        remove(qw);
    }

    @Override
    public SystemUserPhone getByUserId(long userId) {
        final QueryWrapper<SystemUserPhone> qw = new QueryWrapper<>();
        qw.eq("user_id", userId);
        return getOne(qw);
    }

    @Override
    public SystemUserPhone getByPhoneNumber(String phoneNumber) {
        final QueryWrapper<SystemUserPhone> qw = new QueryWrapper<>();
        qw.eq("phone", phoneNumber);
        return getOne(qw);
    }

    /**
     * 生成绑定冷却key
     *
     * @param phoneNumber 手机号
     * @return key
     */
    private static String obtainKey(String phoneNumber) {
        return String.format("aliyun:cooldown:bind:%s", phoneNumber);
    }
}
