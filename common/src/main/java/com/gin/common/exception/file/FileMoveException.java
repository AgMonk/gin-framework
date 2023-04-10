package com.gin.common.exception.file;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

/**
 * 文件移动异常
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 10:38
 */
@Getter
public class FileMoveException extends IOException {
    public static final String DEFAULT_MESSAGE = "文件移动失败";
    private final File src;
    private final File dest;


    public FileMoveException(File src, File dest) {
        super(DEFAULT_MESSAGE);
        this.src = src;
        this.dest = dest;
    }

    public FileMoveException(String message, File src, File dest) {
        super(message);
        this.src = src;
        this.dest = dest;
    }

    public FileMoveException(String message, Throwable cause, File src, File dest) {
        super(message, cause);
        this.src = src;
        this.dest = dest;
    }

    public FileMoveException(Throwable cause, File src, File dest) {
        super(DEFAULT_MESSAGE, cause);
        this.src = src;
        this.dest = dest;
    }
}
