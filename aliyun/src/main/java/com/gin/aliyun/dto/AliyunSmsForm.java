package com.gin.aliyun.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;

/**
 * 发送短信请求
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/12/11 13:44
 **/
@Getter
@Setter
@Schema(description = "表单对象:发送短信请求")
public class AliyunSmsForm {
    @Schema(description = "手机号")
    @NotEmpty
    String phoneNumber;
    @Schema(description = "签名")
    @NotEmpty
    String signName;
    @Schema(description = "模板CODE")
    @NotEmpty
    String templateCode;
    @Schema(description = "模板参数")
    HashMap<String, Object> templateParam;
}