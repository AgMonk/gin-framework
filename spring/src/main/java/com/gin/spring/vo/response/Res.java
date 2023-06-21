package com.gin.spring.vo.response;

import com.gin.common.constant.Messages;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * 统一响应对象
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/5 10:21
 */
@Schema(description = "统一响应对象")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Res<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 0L;
    @Schema(description = "消息")
    String message;
    @Schema(description = "数据")
    T data;
    @Schema(description = "时间戳(UNIX秒)")
    Long timestamp = System.currentTimeMillis() / 1000;

    public Res(String message, T data) {
        this(message, data, System.currentTimeMillis() / 1000);
    }

    public static <T> Res<T> of(T data) {
        return of(data, data == null ? Messages.DATA_NOT_FOUND : "ok");
    }

    public static <T> Res<T> of(T data, String message) {
        return new Res<>(message, data);
    }

}

