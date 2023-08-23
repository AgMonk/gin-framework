package com.gin.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 身份证校验工具类
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/8/23 10:57
 **/
public class IdCardUtils {
    public static final Pattern PATTERN_15 = Pattern.compile("^\\d{15}$");
    public static final Pattern PATTERN_18 = Pattern.compile("^\\d{17}[0-9Xx]$");
    /**
     * 校验权重
     */
    public static final int[] weights = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    /**
     * 校验位
     */
    public static final char[] verifyCode = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    /**
     * 地区码
     */
    public final static Map<Integer, String> zoneMap = new HashMap<>();

    static {
        zoneMap.put(11, "北京");
        zoneMap.put(12, "天津");
        zoneMap.put(13, "河北");
        zoneMap.put(14, "山西");
        zoneMap.put(15, "内蒙古");
        zoneMap.put(21, "辽宁");
        zoneMap.put(22, "吉林");
        zoneMap.put(23, "黑龙江");
        zoneMap.put(31, "上海");
        zoneMap.put(32, "江苏");
        zoneMap.put(33, "浙江");
        zoneMap.put(34, "安徽");
        zoneMap.put(35, "福建");
        zoneMap.put(36, "江西");
        zoneMap.put(37, "山东");
        zoneMap.put(41, "河南");
        zoneMap.put(42, "湖北");
        zoneMap.put(43, "湖南");
        zoneMap.put(44, "广东");
        zoneMap.put(45, "广西");
        zoneMap.put(46, "海南");
        zoneMap.put(50, "重庆");
        zoneMap.put(51, "四川");
        zoneMap.put(52, "贵州");
        zoneMap.put(53, "云南");
        zoneMap.put(54, "西藏");
        zoneMap.put(61, "陕西");
        zoneMap.put(62, "甘肃");
        zoneMap.put(63, "青海");
        zoneMap.put(64, "宁夏");
        zoneMap.put(65, "新疆");
        zoneMap.put(71, "台湾");
        zoneMap.put(81, "香港");
        zoneMap.put(82, "澳门");
        zoneMap.put(91, "国外");
    }


    /**
     * 校验身份证合法性
     *
     * @param idCardNo 身份证号
     * @return 是否合法
     */
    public static Result check(String idCardNo) {

        final boolean is15 = PATTERN_15.matcher(idCardNo).find();
        final boolean is18 = PATTERN_18.matcher(idCardNo).find();
        // 基础校验
        if (!is15 && !is18) {
            return new Result(1, false, "身份证应当为\"15位数字\"或\"17位数字+1位数字或x\"");
        }
        // 校验地区
        final int zoneId = Integer.parseInt(idCardNo.substring(0, 2));
        if (!zoneMap.containsKey(zoneId)) {
            return new Result(2, false, "地区码不存在: " + zoneId);
        }
        // 生日校验
        try {
            checkBirthday(idCardNo);
        } catch (RuntimeException e) {
            return new Result(3, false, e.getMessage() + " 不是一个合法的日期");
        }
        // 对18位身份证计算校验位
        if (!checkVerifyCode(idCardNo)) {
            return new Result(4, false, "校验位错误");
        }

        return new Result(0, true, "OK");
    }

    /**
     * 校验身份证中的生日
     *
     * @param idCardNo 身份证
     * @throws RuntimeException 抛出异常表示校验失败
     */
    private static void checkBirthday(String idCardNo) {
        String birthday = idCardNo.length() == 15 ? ("19" + idCardNo.substring(6, 12)) : idCardNo.substring(6, 14);
        SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd");
        // 严格校验
        sd.setLenient(false);
        try {
            sd.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException(birthday, e);
        }
    }

    /**
     * 检查18位身份证的校验位
     *
     * @param idCardNo 身份证
     * @return 是否通过
     */
    private static boolean checkVerifyCode(String idCardNo) {
        if (idCardNo.length() != 18) {
            return true;
        }
        String card = idCardNo.toUpperCase();
        int sum = 0;
        final char[] cs = card.toCharArray();
        for (int i = 0; i < cs.length - 1; i++) {
            sum += (cs[i] - '0') * weights[i];
        }
        return verifyCode[sum % 11] == cs[cs.length - 1];
    }

    public static class Result {
        public final int code;
        public final boolean legal;
        public final String message;

        public Result(int code, boolean legal, String message) {
            this.code = code;
            this.legal = legal;
            this.message = message;
        }

        @Override
        public String toString() {
            return String.format("%d %s - %s", code, legal, message);
        }
    }
}
