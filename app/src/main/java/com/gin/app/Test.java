package com.gin.app;

import com.gin.common.exception.file.DirCreateException;
import com.gin.common.exception.file.FileExistsException;
import com.gin.common.exception.file.FileMoveException;
import com.gin.common.exception.file.FileNotExistsException;
import com.gin.common.utils.FileUtils;
import org.aspectj.weaver.patterns.PatternNode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @since : 2023/11/27 16:11
 * @author : ginstone
 * @version : v1.0.0
 **/
public class Test {
    public static final Pattern PATTERN = Pattern.compile("(\\d+)_p(\\d+)");
    public static void main(String[] args) throws IOException {
        File dir = new File("H:\\新建文件夹\\pixiv");
        final int step = 200;
        int i = 0;
        int start = 0;
        final ArrayList<File> files = FileUtils.listAllFiles(dir);

        files.sort((o1, o2) -> {
            final Matcher m1 = PATTERN.matcher(o1.getName());
            final Matcher m2 = PATTERN.matcher(o2.getName());
            if (m1.find() && m2.find()){
                final long c1 = Long.parseLong(m1.group(1)) + Long.parseLong(m1.group(2));
                final long c2 = Long.parseLong(m2.group(1)) + Long.parseLong(m2.group(2));
                return Math.toIntExact(c1 - c2);
            }else{
                return 0;
            }
        });

        while (start < files.size() - 1) {
            File destDir = new File(String.format("h:/pixiv/%06d", i));

            int end = Math.min(files.size(), start + step);
            for (File f : files.subList(start, end)) {
                try {
                    FileUtils.move2Dir(f, destDir);
                } catch (FileExistsException e) {
                    System.err.println("文件已存在: "+e.getFile());
                }
            }
            i++;
            start += step;
        }

    }
}
