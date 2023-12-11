package com.gin.aliyun.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Getter;
import lombok.Setter;

/**
 * 阿里云短信服务响应对象
 */
@Getter
@Setter
public class AliyunSmsResponse {
    @JsonAlias("Message")
    String message;
    @JsonAlias("RequestId")
    String requestId;
    @JsonAlias("BizId")
    String bizId;
    @JsonAlias("Code")
    String code;

    public boolean isSuccess() {
        return "OK".equals(code);
    }
}