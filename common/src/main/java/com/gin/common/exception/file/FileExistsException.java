package com.gin.common.exception.file;

import lombok.Getter;

import java.io.File;
import java.io.IOException;

/**
 * 文件已存在异常
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 10:33
 */
@Getter
public class FileExistsException extends IOException {
    public static final String DEFAULT_MESSAGE = "文件或目录已存在";
    private final File file;

    public FileExistsException(File file) {
        super(DEFAULT_MESSAGE);
        this.file = file;
    }

    public FileExistsException(String message, File file) {
        super(message);
        this.file = file;
    }

    public FileExistsException(String message, Throwable cause, File file) {
        super(message, cause);
        this.file = file;
    }

    public FileExistsException(Throwable cause, File file) {
        super(DEFAULT_MESSAGE, cause);
        this.file = file;
    }
}
