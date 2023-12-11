package com.gin.aliyun.service;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.aliyun.config.AliyunCode;
import com.gin.aliyun.dto.AliyunSmsResponse;
import com.gin.spring.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 阿里云验证码服务
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 14:41
 **/
@Service
@RequiredArgsConstructor
public class AliyunCodeService {
    /**
     * 验证码过期时长
     */
    public static final int CODE_TIMEOUT = 5;
    private final AliyunSmsService aliyunSmsService;
    private final AliyunCode aliyunCode;

    private final RedisTemplate<String, String> stringTemplate;

    /**
     * 向指定手机号发送验证码, 并将验证码保存到Redis
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     */
    public void sendCode(String phoneNumber, String code) throws ClientException, JsonProcessingException {
        final String key = obtainKey(phoneNumber);
        // 检查验证码是否已存在
        final Long expire = stringTemplate.getExpire(key, TimeUnit.SECONDS);
        // 如果验证码已存在 且剩余时间大于4分钟，报错
        if (expire != null && expire > TimeUnit.MINUTES.toSeconds(CODE_TIMEOUT - 1)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "请等一会再获取验证码");
        }

        // 发送验证码
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        final AliyunSmsResponse response = aliyunSmsService.send(phoneNumber, aliyunCode.getSignName(), aliyunCode.getTemplateCode(), map);
        // 如果验证码发送成功，将其保存到redis
        if (response.isSuccess()) {
            stringTemplate.opsForValue().set(key, code, CODE_TIMEOUT, TimeUnit.MINUTES);
        } else {
            throw BusinessException.of(HttpStatus.INTERNAL_SERVER_ERROR, "验证码发送失败, 请稍候重试或联系管理员");
        }
    }

    /**
     * 校验验证码, 如果校验通过则移除redis中保存的验证码
     *
     * @param phoneNumber 手机号
     * @param code        验证码
     */
    public void checkCode(String phoneNumber, String code) {
        final String key = obtainKey(phoneNumber);

        final String value = stringTemplate.opsForValue().get(key);
        if (ObjectUtils.isEmpty(value)) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "验证码已过期或不存在");
        }
        final boolean passed = value.equalsIgnoreCase(code);
        // 校验通过，移除redis中保存的验证码
        if (passed) {
            stringTemplate.delete(key);
        } else {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "验证码错误");
        }
    }

    /**
     * 生成Redis中保存验证码的key
     *
     * @param phoneNumber 手机号
     * @return key
     */
    private static String obtainKey(String phoneNumber) {
        return String.format("aliyun:code:%s", phoneNumber);
    }
}
