package com.gin.common.utils;

import org.springframework.cglib.beans.BeanMap;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 09:56
 */
public class StrUtils {
    public static final Pattern NUMBER_PATTERN= Pattern.compile("^\\d+$");

    /**
     * 字符串是否为纯数字
     * @param s 字符串
     * @return 是否为纯数字
     */
    public static boolean isNumber(String s){
        return NUMBER_PATTERN.matcher(s).find();
    }

    /**
     * 将对象中的字段按照模板生成格式化文本，使用 {字段名} 占位
     * @param template 模板
     * @param obj      需要输出的对象
     * @return 格式化文本
     */
    public static String format(String template, Object obj) {
        final BeanMap beanMap = BeanMap.create(obj);
        final HashMap<String, String> map = new HashMap<>();

        //noinspection unchecked
        beanMap.forEach((o, o2) -> {
            if (o instanceof String s) {
                map.put(s, String.valueOf(o2));
            }
        });

        for (Map.Entry<String, String> entry : map.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            template = template.replace(String.format("{%s}", k), v);
        }
        return template;
    }

    /**
     * 根据包全名，返回简单名
     */
    public static String getSimplePackageName(String fullName) {
        final String[] split = fullName.split("\\.");
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            if (i == split.length - 1) {
                sb.append(split[i]);
            } else {
                sb.append(split[i].charAt(0)).append(".");
            }
        }
        return sb.toString();
    }

    /**
     * 字符串裁剪(自动处理:越界,负数,位置反写)
     * @param src   源字符串
     * @param start 开始位置
     * @param end   结束位置
     * @return 新字符串
     */
    public static String sub(String src, int start, int end) {
        if (src == null) {
            return null;
        }
        //源字符串长度
        final int length = src.length();
        //越界
        start = Math.min(start, length);
        end = Math.min(end, length);
        //负数
        start += start < 0 ? length : 0;
        end += end < 0 ? length : 0;
        // 位置反写
        final int beginIndex = Math.min(start, end);
        final int endIndex = Math.max(start, end);
        //裁剪
        return src.substring(beginIndex, endIndex);
    }

    /**
     * 生成UUID
     * @return UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString();
    }

}   
