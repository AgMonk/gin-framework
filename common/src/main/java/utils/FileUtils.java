package utils;

import exception.file.*;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/22 17:16
 */
@Slf4j
public class FileUtils {

    public static final String DOT = ".";
    public static final String PATH_DELIMITER = "/";

    /**
     * 断言文件存在
     * @param file 文件
     * @throws FileNotExistsException 异常
     */
    public static void assertExists(@NotNull File file) throws FileNotExistsException {
        assertNotNull(file);
        if (!file.exists()) {
            throw new FileNotExistsException(file);
        }
    }

    /**
     * 断言文件不存在
     * @param file 文件
     * @throws FileExistsException 异常
     */
    public static void assertNotExists(@NotNull File file) throws FileExistsException {
        assertNotNull(file);
        if (!file.exists()) {
            throw new FileExistsException(file);
        }
    }

    /**
     * 断言文件不是null
     * @param file 文件
     */
    public static void assertNotNull(File file) {
        if (file == null) {
            throw new RuntimeException("文件为null");
        }
    }

    /**
     * 移除文件名中的非法字符
     * @param filename 文件名
     */
    public static String cleanInvalid(@NotNull String filename) {
        return replaceInvalid(filename, "");
    }

    /**
     * 复制文件 使用 CHANNEL 方法
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException 异常
     */
    public static void copyFile(@NotNull File src, @NotNull File dest) throws IOException {
        copyFile(src, dest, CopyMethod.CHANNEL);
    }

    /**
     * 复制文件
     * @param src        源文件
     * @param dest       目标文件
     * @param copyMethod 复制方法 默认为  CHANNEL
     * @throws IOException 异常
     */
    public static void copyFile(@NotNull File src, @NotNull File dest, @NotNull CopyMethod copyMethod) throws IOException {
        assertExists(src);
        assertNotExists(dest);
        switch (copyMethod) {
            case JAVA7 -> copyFileUsingJava7Files(src, dest);
            case STREAM -> copyFileUsingFileStreams(src, dest);
            default -> copyFileUsingFileChannels(src, dest);
        }
    }

    /**
     * 根据file是目录还是文件调用不同的删除方法
     * @param file 目录或文件
     * @throws IOException 异常
     */
    public static void delete(@NotNull File file) throws IOException {
        assertExists(file);
        if (file.isDirectory()) {
            deleteDir(file);
        } else {
            deleteFile(file);
        }
    }

    /**
     * 递归删除目录下的所有文件
     * @param dir 目录
     * @throws IOException 异常
     */
    public static void deleteDir(@NotNull File dir) throws IOException {
        final ArrayList<File> files = listAllFiles(dir);
        for (int i = files.size() - 1; i >= 0; i--) {
            final File item = files.get(i);
            delete(item);
        }
    }

    /**
     * 删除一个文件
     * @param file 文件
     * @throws FileNotExistsException 文件不存在异常
     * @throws FileDeleteException 文件删除异常
     */
    public static void deleteFile(@NotNull File file) throws FileNotExistsException, FileDeleteException {
        assertExists(file);
        if (!file.delete()) {
            throw new FileDeleteException(file);
        }
        log.info("已删除文件:" + file.getPath());
    }

    /**
     * 返回文件的后缀名(小写)
     * @param filename 文件名
     * @return 后缀名(小写)
     */
    @NotNull
    public static String getFileExtName(@NotNull String filename) {
        if (!filename.contains(DOT)) {
            return "";
        }
        return filename.substring(filename.lastIndexOf(DOT) + 1).toLowerCase();
    }

    /**
     * 返回文件的主文件名(排除后缀部分)
     * @param filename 文件名
     * @return 主文件名
     */
    @NotNull
    public static String getFileMainName(@NotNull String filename) {
        return filename.substring(0, filename.lastIndexOf(DOT) + 1);
    }

    /**
     * 递归扫描目标目录中的所有文件
     * @param dir 目录
     * @return 文件列表
     * @throws IOException 异常
     */
    @NotNull
    public static ArrayList<File> listAllFiles(@NotNull File dir) throws IOException {
        return listAllFiles(dir, false);
    }

    /**
     * 递归扫描目标目录中的所有文件和目录
     * @param dir        目录
     * @param includeDir 是否把目录也添加到列表中
     * @return 文件列表
     * @throws IOException 异常
     */
    @NotNull
    public static ArrayList<File> listAllFiles(@NotNull File dir, boolean includeDir) throws IOException {
        final ArrayList<File> files = listFiles(dir);
        final ArrayList<File> all = new ArrayList<>();
        for (File file : files) {
            if (!file.isDirectory()) {
                //不是目录 添加到列表
                all.add(file);
            } else {
                // 是目录
                if (includeDir) {
                    all.add(file);
                }
                all.addAll(listAllFiles(file, includeDir));
            }
        }
        return all;
    }

    /**
     * 列出指定目录下的所有文件/目录
     * @param dir 目录
     * @return 文件列表
     * @throws IOException 异常
     */
    @NotNull
    public static ArrayList<File> listFiles(@NotNull File dir) throws IOException {
        assertExists(dir);
        if (!dir.isDirectory()) {
            throw new IOException("该文件不是目录:" + dir.getPath());
        }
        final File[] files = dir.listFiles();
        if (files == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(List.of(files));
    }

    /**
     * 递归创建目录
     * @param dir 文件
     * @throws DirCreateException 异常
     * @throws FileExistsException 异常
     */
    public static void mkdir(@NotNull File dir) throws DirCreateException, FileExistsException {
        if (dir.exists() && !dir.isDirectory()) {
            throw new FileExistsException(dir);
        }
        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                throw new DirCreateException(dir);
            } else {
                log.info("创建目录:" + dir.getPath());
            }
        }
    }

    /**
     * 移动文件
     * @param src  源文件
     * @param dest 目标文件
     * @throws FileMoveException 异常
     * @throws FileNotExistsException 异常
     * @throws FileExistsException 异常
     * @throws DirCreateException 异常
     */
    public static void move(
            @NotNull File src,
            @NotNull File dest
    ) throws FileMoveException, FileNotExistsException, FileExistsException, DirCreateException {
        assertExists(src);
        assertNotExists(dest);
        mkdir(dest.getParentFile());
        if (!src.renameTo(dest)) {
            throw new FileMoveException(src, dest);
        }
        log.info("已移动文件 {} -> {}", src.getPath(), dest.getPath());
    }

    /**
     * 将文件移动到一个指定目录
     * @param src 源文件
     * @param dir 目标目录
     * @throws FileMoveException 异常
     * @throws FileNotExistsException 异常
     * @throws FileExistsException 异常
     * @throws DirCreateException 异常
     */
    public static void move2Dir(
            @NotNull File src,
            @NotNull File dir
    ) throws FileNotExistsException, DirCreateException, FileMoveException, FileExistsException {
        move(src, new File(dir.getPath() + PATH_DELIMITER + src.getName()));
    }

    /**
     * 替换文件名中的非法字符
     * @param filename    文件名
     * @param replacement 替换成的字符
     */
    public static String replaceInvalid(String filename, String replacement) {
        if (filename == null) {
            return null;
        }
        return filename.replaceAll("[?|<>\"*:/\\\\]", replacement);
    }

    /**
     * 使用FileChannel复制 能吃到3种方式的缓存
     * @param src  源文件
     * @param dest 目标文件
     * @throws IOException 异常
     */
    private static void copyFileUsingFileChannels(@NotNull File src, @NotNull File dest) throws IOException {
        try (FileChannel inputChannel = new FileInputStream(src).getChannel(); FileChannel outputChannel = new FileOutputStream(dest).getChannel()) {
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        }
    }

    /**
     * 使用FileStreams复制 不能吃到任何缓存
     * @param src  源文件
     * @param dest 目标文件
     */
    private static void copyFileUsingFileStreams(@NotNull File src, @NotNull File dest) throws IOException {
        try (InputStream input = new FileInputStream(src); OutputStream output = new FileOutputStream(dest)) {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) > 0) {
                output.write(buf, 0, bytesRead);
            }
        }
    }

    /**
     * 使用Java7的Files类复制  能吃到3种方式的缓存
     * @param source 源文件
     * @param dest   目标文件
     */
    private static void copyFileUsingJava7Files(@NotNull File source, @NotNull File dest) throws IOException {
        Files.copy(source.toPath(), dest.toPath());
    }

    public enum CopyMethod {
        STREAM, CHANNEL, JAVA7
    }

}   
