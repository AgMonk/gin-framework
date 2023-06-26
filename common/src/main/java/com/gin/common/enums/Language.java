package com.gin.common.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 常见语言，及其Unicode范围
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/6/26 14:37
 */
public enum Language {
    //中文
    cn("4E00-9FA5", "9FA6-9FFF", "3400-4DBF", "20000-2A6DF", "2A700-2B739", "2B740-2B81D", "2B820-2CEA1",
            "2CEB0-2EBE0", "30000-3134A", "31350-323AF", "2F00-2FD5", "2E80-2EF3", "F900-FAD9", "2F800-2FA1D",
            "E815-E86F", "E400-E5E8", "E600-E6CF", "31C0-31E3", "2FF0-2FFB", "3105-312F", "31A0-31BA"
    ),
    //日文
    jp("3040-309F", "30A0-30FF", "31F0-31FF"),

    //英文
    en("0041-005A", "0061-007A"),
    //数字
    number("0030-0039"),
    //韩朝
    ko("1100-11FF", "3130-318F", "AC00-D7AF"),
//    其他
    other(),
    ;
    public final String[] range;

    Language(String... range) {
        this.range = range;
    }

    /**
     * 返回代码点所属的语言
     * @param codePoint 代码点
     * @return 字符语言
     */
    public static Language findLanguage(int codePoint){
        for (Language lang : values()) {
            if (lang.inRange( codePoint)) {
                return lang;
            }
        }
        return other;
    }

    /**
     * 判断代码点的语言
     *
     * @param codePoint 代码点
     * @return 是否为该语言
     */
    public boolean inRange(int codePoint) {
        if (this.range == null){
            return false;
        }

        for (String r : this.range) {
            final List<Integer> parse = Arrays.stream(r.split("-")).map(i -> Integer.parseInt(i, 16)).collect(Collectors.toList());
            final Integer min = parse.get(0);
            final Integer max = parse.get(1);
            if (codePoint >= min && codePoint <= max) {
                return true;
            }
        }
        return false;
    }
}
