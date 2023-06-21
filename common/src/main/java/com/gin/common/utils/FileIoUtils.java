package com.gin.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gin.jackson.utils.JacksonUtils.MAPPER;


/**
 * 文件IO工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/22 16:07
 */
public class FileIoUtils {

    /**
     * 从文件获取 BufferedInputStream
     * @param file 文件
     * @return BufferedInputStream
     */

    public static BufferedInputStream getInputStream(File file) throws FileNotFoundException {
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 从文件获取 BufferedOutputStream
     * @param file   文件
     * @param append 追加模式
     * @return BufferedOutputStream
     */

    public static BufferedOutputStream getOutputStream(File file, boolean append) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file, append));
    }

    /**
     * 从文件获取 BufferedReader
     * @param file 文件
     * @return BufferedReader
     */

    public static BufferedReader getReader(File file) throws FileNotFoundException {
        return new BufferedReader(new FileReader(file));
    }

    /**
     * 从文件获取 PrintWriter
     * @param file   文件
     * @param append 追加模式
     * @return PrintWriter
     */

    public static PrintWriter getWriter( File file, boolean append) throws IOException {
        FileUtils.mkdir(file.getParentFile());
        return new PrintWriter(new FileWriter(file, append));
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file      文件
     * @param valueType 类对象
     * @param <T>       T
     * @return 指定类的对象
     */
    public static <T> T readObj(File file, Class<T> valueType) throws IOException {
        try (BufferedReader reader = getReader(file)) {
            return MAPPER.readValue(reader, valueType);
        }
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file     文件
     * @param javaType javaType 构造泛型:MAPPER.getTypeFactory().constructParametricType
     * @param <T>      T
     * @return 指定类对象
     */
    public static <T> T readObj(File file, JavaType javaType) throws IOException {
        try (BufferedReader reader = getReader(file)) {
            return MAPPER.readValue(reader, javaType);
        }
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file          文件
     * @param typeReference typeReference
     * @param <T>           T
     * @return 指定类对象
     */
    public static <T> T readObj(File file, TypeReference<T> typeReference) throws IOException {
        try (BufferedReader reader = getReader(file)) {
            return MAPPER.readValue(reader, typeReference);
        }
    }

    /**
     * 从文件中读取json字符串并解析为对象
     * @param file 文件
     * @param func 通过 TypeFactory 的 constructParametricType 等方法创建 JavaType
     * @param <T>  T
     * @return 指定类对象
     */
    public static <T> T readObj(File file,  Function<TypeFactory, JavaType> func) throws IOException {
        return readObj(file, func.apply(MAPPER.getTypeFactory()));
    }

    /**
     * 以字符串形式读取一个文件
     * @param file 文件
     * @return 字符串
     */
    public static String readStr(File file) throws FileNotFoundException {
        return getReader(file).lines().collect(Collectors.joining("\n"));
    }

    /**
     * 将对象转换成json格式写入文件;如果文件已存在将会覆盖文件
     * @param file 文件
     * @param obj  对象
     */
    public static void writeObj(File file, Object obj) throws IOException {
        try (PrintWriter writer = getWriter(file, false)) {
            MAPPER.writeValue(writer, obj);
        }
    }

    /**
     * 写一个字符串到文件
     * @param file   文件
     * @param s      字符串
     * @param append 追加模式
     */
    public static void writeStr(File file, String s, boolean append) throws IOException {
        try (PrintWriter writer = getWriter(file, append)) {
            writer.write(s);
        }
    }

    public static void main(String[] args) throws IOException {
    }
}
