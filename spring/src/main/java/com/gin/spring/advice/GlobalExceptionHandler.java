package com.gin.spring.advice;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.gin.spring.exception.BusinessException;
import com.gin.spring.exception.MyFieldError;
import com.gin.spring.vo.response.Res;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * 统一异常处理
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/10 11:16
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    /**
     * 唯一约束异常的描述正则
     */
    public static final Pattern DUPLICATE_KEY_PATTERN = Pattern.compile("Duplicate entry '(.+?)' for key");

    /**
     * 兜底异常处理
     *
     * @param e 其他异常
     * @return 处理
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Res<?>> exceptionHandler(Exception e) {
        e.printStackTrace();
        return new ResponseEntity<>(Res.of(null, "服务器错误 请通知管理员"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * 数字格式不正确
     *
     * @param e 其他异常
     * @return 处理
     */
    @ExceptionHandler({NumberFormatException.class})
    public ResponseEntity<Res<?>> exceptionHandler(NumberFormatException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "数字格式不正确"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 方法参数类型不匹配
     *
     * @param e 其他异常
     * @return 处理
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Res<?>> exceptionHandler(MethodArgumentTypeMismatchException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "方法参数类型不匹配"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 触发唯一约束异常
     *
     * @param e 唯一约束异常
     * @return
     */
    @ExceptionHandler({DuplicateKeyException.class})
    public ResponseEntity<Res<?>> exceptionHandler(DuplicateKeyException e) {
        final String message = e.getMessage();
        final Matcher matcher = DUPLICATE_KEY_PATTERN.matcher(message);
        if (matcher.find()) {
            final String msg = String.format("'%s' 已存在", matcher.group(1));
            return new ResponseEntity<>(Res.of(null, msg), HttpStatus.BAD_REQUEST);
        } else {
            e.printStackTrace();
        }
        return new ResponseEntity<>(Res.of(e.getMessage(), "唯一约束异常"), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Res<?>> exceptionHandler(HttpMessageNotReadableException e) {
        if (e.getCause() instanceof UnrecognizedPropertyException ee) {
            final ArrayList<String> data = new ArrayList<>();
            data.add("不存在的字段: " + ee.getPropertyName());
            data.add("合法字段列表: " + ee.getKnownPropertyIds());
            data.add("目标类型:" + ee.getTargetType());
            data.add(ee.getLocalizedMessage());
            return new ResponseEntity<>(Res.of(data, "请求参数非法"), HttpStatus.BAD_REQUEST);
        }
        if (e.getCause() instanceof InvalidFormatException ex) {
            return new ResponseEntity<>(Res.of(ex.getLocalizedMessage(), "非法的参数格式"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "消息读取失败"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<Res<String>> exceptionHandler(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "请求缺少必须的参数"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MaxUploadSizeExceededException.class})
    public ResponseEntity<Res<String>> exceptionHandler(MaxUploadSizeExceededException e) {
        return new ResponseEntity<>(Res.of("文件大小超出限制", e.getLocalizedMessage()), HttpStatus.BAD_REQUEST);
    }

    /**
     * 参数校验处理
     *
     * @param e 阐述校验异常
     * @return 处理
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Res<String>> exceptionHandler(ConstraintViolationException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "参数校验异常"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 业务异常处理
     *
     * @param e 业务异常
     * @return 处理
     */
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<Res<List<String>>> exceptionHandler(BusinessException e) {
        log.warn("code: {} , 业务异常: {} , value: {}", e.getHttpStatus().value(), e.getTitle(), e.getMessages());
        return new ResponseEntity<>(Res.of(e.getMessages(), e.getTitle()), e.getHttpStatus());
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Res<?>> exceptionHandler(BindException e) {
        final List<MyFieldError> fieldErrors = e.getFieldErrors().stream().map(MyFieldError::new).collect(Collectors.toList());
        if (fieldErrors.size() == 0) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(Res.of(fieldErrors, "参数校验错误"), HttpStatus.BAD_REQUEST);
    }

    /**
     * 不支持的请求方法
     *
     * @param e 其他异常
     * @return 处理
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Res<String>> exceptionHandler(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(Res.of(e.getLocalizedMessage(), "不支持的请求方法"), HttpStatus.BAD_REQUEST);
    }
}   
