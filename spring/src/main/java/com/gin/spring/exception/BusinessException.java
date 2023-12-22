package com.gin.spring.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * 业务异常
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:05
 */
@Getter
public class BusinessException extends RuntimeException {

    final HttpStatus httpStatus;
    final String title;
    final List<String> messages;

    /**
     * 构造方法
     *
     * @param httpStatus 状态码
     * @param title      标题
     * @param messages   消息
     */
    public BusinessException(HttpStatus httpStatus, String title, List<String> messages) {
        this.httpStatus = httpStatus;
        this.title = title;
        this.messages = messages;
    }

    public static BusinessException of(HttpStatus httpStatus, String title, List<String> messages) {
        return new BusinessException(httpStatus, title, messages);
    }

    public static BusinessException forbidden(String title, String... messages) {
        return of(HttpStatus.FORBIDDEN, title, messages);
    }

    public static BusinessException badRequest(String title, String... messages) {
        return of(HttpStatus.BAD_REQUEST, title, messages);
    }

    public static BusinessException of(HttpStatus httpStatus, String title, String... messages) {
        return new BusinessException(httpStatus, title, List.of(messages));
    }


}
