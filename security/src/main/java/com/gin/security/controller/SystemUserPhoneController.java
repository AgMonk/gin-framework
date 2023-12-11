package com.gin.security.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.common.constant.ApiPath;
import com.gin.security.entity.SystemUserPhone;
import com.gin.security.service.SystemUserPhoneService;
import com.gin.security.utils.MySecurityUtils;
import com.gin.spring.annotation.MyRestController;
import com.gin.spring.exception.BusinessException;
import com.gin.spring.vo.response.Res;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/9/26 16:37
 **/
@Slf4j
@MyRestController(SystemUserPhoneController.API_PREFIX)
@Tag(name = SystemUserPhoneController.GROUP_NAME)
@RequiredArgsConstructor
public class SystemUserPhoneController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = SystemUserController.API_PREFIX + "/phone";
    /**
     * 接口文档分组名称
     */
    public static final String GROUP_NAME = "用户手机绑定";
    /**
     * 本接口使用的主Service类
     */
    private final SystemUserPhoneService service;

    @PostMapping("sendCode")
    @Operation(summary = "发送验证码")
    public Res<Void> postSendCode(@RequestParam @Pattern(regexp = "^1[3-9][0-9]{9}$") String phoneNumber) throws ClientException, JsonProcessingException {
        service.sendCode(phoneNumber);
        return Res.success("验证码已发送");
    }

    @PostMapping("checkCode")
    @Operation(summary = "校验验证码")
    public Res<Void> postCheckCode(@RequestParam @Pattern(regexp = "^1[3-9][0-9]{9}$") String phoneNumber, @RequestParam String code) {
        service.checkCode(phoneNumber, code);
        return Res.success("绑定成功");
    }

    @GetMapping(ApiPath.GET)
    @Operation(summary = "查询我的绑定手机")
    public Res<String> getGet() {
        final SystemUserPhone userPhone = service.getByUserId(MySecurityUtils.currentUserDetails().getId());
        if (userPhone == null) {
            throw BusinessException.of(HttpStatus.BAD_REQUEST, "尚未绑定手机");
        }

        return Res.of(userPhone.getPhone().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
    }

    @PostMapping("unbind")
    @Operation(summary = "解绑手机", description = "绑定手机有冷却时间，解绑需谨慎")
    public Res<String> postUnbind() {
        service.unbindByUserId(MySecurityUtils.currentUserDetails().getId());
        return Res.of("已解绑");
    }
}