package com.gin.aliyun.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.aliyun.docket.AliyunDocket;
import com.gin.aliyun.dto.AliyunSmsForm;
import com.gin.aliyun.dto.AliyunSmsResponse;
import com.gin.aliyun.service.AliyunCodeService;
import com.gin.aliyun.service.AliyunSmsService;
import com.gin.spring.annotation.MyRestController;
import com.gin.spring.vo.response.Res;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Random;

/**
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/9/26 16:37
 **/
@Slf4j
@MyRestController(AliyunSmsController.API_PREFIX)
@Tag(name = AliyunSmsController.GROUP_NAME)
@RequiredArgsConstructor
public class AliyunSmsController {
    /**
     * 接口路径前缀
     */
    public static final String API_PREFIX = AliyunDocket.API_PREFIX + "/sms";
    /**
     * 接口文档分组名称
     */
    public static final String GROUP_NAME = "短信服务";
    /**
     * 本接口使用的主Service类
     */
    private final AliyunSmsService service;

    private final AliyunCodeService aliyunCodeService;

    @PostMapping("send")
    @Operation(summary = "发送短信")
    public Res<AliyunSmsResponse> post(@RequestBody @Validated AliyunSmsForm form) throws ClientException, JsonProcessingException {
        return Res.of(service.send(form.getPhoneNumber(), form.getSignName(), form.getTemplateCode(), form.getTemplateParam()));
    }


    @PostMapping("sendCode")
    @Operation(summary = "发送验证码")
    public Res<Void> postSendCode(@RequestParam @Pattern(regexp = "^1[3-9][0-9]{9}$") String phoneNumber) throws ClientException, JsonProcessingException {
        final Random random = new Random();
        final int number = random.nextInt(1000, 9999);

        aliyunCodeService.sendCode(phoneNumber, String.valueOf(number));

        return Res.success();
    }

    @PostMapping("checkCode")
    @Operation(summary = "校验验证码")
    public Res<Void> postCheckCode(@RequestParam @Pattern(regexp = "^1[3-9][0-9]{9}$") String phoneNumber, @RequestParam String code) {
        aliyunCodeService.checkCode(phoneNumber, code);
        return Res.success();
    }
}