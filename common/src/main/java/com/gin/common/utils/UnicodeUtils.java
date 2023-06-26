package com.gin.common.utils;

/**
 * unicode工具类
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/6/26 14:45
 */
public class UnicodeUtils {
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
            //处理
            handler.handle(cpIndex, codePoint, ch);
        }
    }

    public interface Handler {
        /**
         * 处理单个代码点
         *
         * @param index     代码点位置
         * @param codePoint 代码点
         * @param s         该位置上的unicode字符
         */
        void handle(int index, int codePoint, String s);
    }
}   
