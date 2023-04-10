package com.gin.common.exception.file;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

/**
 * 目录创建异常
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 10:41
 */
@Getter
public class DirCreateException extends IOException {
    public static final String DEFAULT_MESSAGE = "目录创建失败";

    private final File dir;

    public DirCreateException(File dir) {
        super(DEFAULT_MESSAGE);
        this.dir = dir;
    }

    public DirCreateException(String message, File dir) {
        super(message);
        this.dir = dir;
    }

    public DirCreateException(String message, Throwable cause, File dir) {
        super(message, cause);
        this.dir = dir;
    }

    public DirCreateException(Throwable cause, File dir) {
        super(DEFAULT_MESSAGE, cause);
        this.dir = dir;
    }
}
