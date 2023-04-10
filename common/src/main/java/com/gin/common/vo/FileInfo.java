package com.gin.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.gin.common.utils.FileUtils;
import com.gin.common.utils.TimeUtils;

import java.io.File;

/**
 * 文件信息
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/1/12 11:10
 */
@Getter
@Schema(description = "文件信息")
@NoArgsConstructor
public class FileInfo {
    private static final long K = 1024;
    private static final long M = K * K;
    private static final long M5 = 5 * M;
    @Schema(description = "文件后缀")
    String ext;
    @JsonIgnore
    File file;
    @Schema(description = "文件名")
    String filename;
    @Schema(description = "最后编辑时间戳(UNIX秒)")
    Long lastModified;
    @Schema(description = "最后编辑日期时间")
    String lastModifiedDatetime;
    @Schema(description = "文件大小(短格式)")
    String shortSize;
    @Schema(description = "文件大小(单位:B)")
    Long size;

    public FileInfo(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        this.size = file.length();
        this.file = file;
        this.ext = FileUtils.getFileExtName(file.getName());
        this.filename = file.getName();

        if (size < K) {
            //小于1K时显示 B
            this.shortSize = size + " B";
        } else if (size < M5) {
            // 小于5M时 显示 KB
            this.shortSize = size * 10 / K / 10.0 + " KB";
        } else {
            // 其他显示 MB
            this.shortSize = size * 10 / M / 10.0 + " MB";
        }

        this.lastModified = file.lastModified() / 1000;
        this.lastModifiedDatetime = TimeUtils.format(file.lastModified() / 1000);
    }

    @Override
    public String toString() {
        return String.format("文件名: %s, 最后编辑时间: %s, 文件大小: %s", this.filename, this.lastModifiedDatetime, this.shortSize);
    }
}
