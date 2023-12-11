package com.gin.aliyun.controller;

import com.aliyuncs.exceptions.ClientException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.gin.aliyun.docket.AliyunDocket;
import com.gin.aliyun.dto.AliyunSmsForm;
import com.gin.aliyun.dto.AliyunSmsResponse;
import com.gin.aliyun.service.AliyunSmsService;
import com.gin.spring.annotation.MyRestController;
import com.gin.spring.vo.response.Res;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

    @PostMapping("send")
    @Operation(summary = "发送短信")
    public Res<AliyunSmsResponse> post(@RequestBody @Validated AliyunSmsForm form) throws ClientException, JsonProcessingException {
        return Res.of(service.send(form.getPhoneNumber(), form.getSignName(), form.getTemplateCode(), form.getTemplateParam()));
    }

}