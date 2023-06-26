package com.gin.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * unicode工具类
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/6/26 14:45
 */
public class UnicodeUtils {
    /**
     * unicode格式
     */
    public static final Pattern UNICODE_PATTERN = Pattern.compile("&#(\\d+);");

    /**
     * 按代码点遍历文本中的字符
     *
     * @param text    文本
     * @param handler 处理方法
     */
    public static void unicodeIterator(String text, Handler handler) {
        //代码点个数
        int cpCount = text.codePointCount(0, text.length());
        for (int cpIndex = 0; cpIndex < cpCount; cpIndex++) {
            //当前代码点起始位置
            final int start = text.offsetByCodePoints(0, cpIndex);
            //下一个代码点起始位置
            final int end = text.offsetByCodePoints(0, cpIndex + 1);
            //当前代码点
            final int codePoint = text.codePointAt(start);
            //当前代码点的字符
            String ch = text.substring(start, end);
            //unicode编码
            String unicode = String.format("&#%d;", codePoint);
            //处理
            handler.handle(cpIndex, codePoint, ch, unicode);
        }
    }

    /**
     * 解码所有 &#(\d+); 格式的unicode字符
     *
     * @return 字符串
     */
    public static String decode(String text) {
        LinkedHashMap<String,String> replacement = new LinkedHashMap<>();
        final Matcher matcher = UNICODE_PATTERN.matcher(text);
        while(matcher.find()){
            final String key = matcher.group();
            final String value = codePointToString(Integer.parseInt(matcher.group(1)));
            replacement.put(key,value);
        }
        for (Map.Entry<String, String> entry : replacement.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            text = text.replace(k,v);
        }
        return text;
    }

    /**
     * 将codePoint转换为字符串
     *
     * @param codePoint 代码点
     * @return 字符串
     */
    public static String codePointToString(int codePoint) {
        return new String(new int[]{codePoint}, 0, 1);
    }

    public interface Handler {
        /**
         * 处理单个代码点
         * @param index     代码点位置
         * @param codePoint 代码点
         * @param s         该位置上的unicode字符
         * @param unicode   unicode编码
         */
        void handle(int index, int codePoint, String s, String unicode);
    }
}   
