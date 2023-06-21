package com.gin.common.utils;

import java.io.File;
import java.io.IOException;

/**
 * 命令行工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/11 15:37
 */
public class ProcessUtils {

    public static Process downloadWithCurl(String url, File dir) throws IOException {
        return new ProcessBuilder("curl", "-fL", "-O", url).directory(dir).start();
    }
}   
