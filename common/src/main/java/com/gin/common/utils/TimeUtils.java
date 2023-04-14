package com.gin.common.utils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.*;

/**
 * 时间工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 11:14
 */
public class TimeUtils {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter DATE_TIME_FORMATTER_2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();
    public static final ZoneId CHINESE_ZONE_ID = ZoneId.of("Asia/Shanghai");
    public static final DateTimeFormatter FULL_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
    /**
     * 千
     */
    private final static long K = 1000L;

    /**
     * 按格式输出一个日期时间
     * @param zdt ZonedDateTime
     * @return 日期时间
     */
    public static String format(ZonedDateTime zdt) {
        return format(zdt, DATE_TIME_FORMATTER);
    }

    /**
     * 按格式输出当前时间
     * @param formatter 格式
     * @return 日期时间
     */
    public static String format(DateTimeFormatter formatter) {
        return format(ZonedDateTime.now(), formatter);
    }

    /**
     * 按格式输出一个日期时间
     * @param formatter 格式
     * @param zdt       ZonedDateTime
     * @return 日期时间
     */
    public static String format(ZonedDateTime zdt, DateTimeFormatter formatter) {
        return formatter.format(zdt);
    }

    /**
     * 按格式输出一个日期时间(单位:秒)
     * @param time 时间戳
     * @return 日期时间
     */
    public static String format(long time) {
        return format(time, TimeUnit.SECONDS);
    }

    /**
     * 按格式输出一个日期时间
     * @param time     时间戳
     * @param timeUnit 时间单位
     * @return 日期时间
     */
    public static String format(long time, TimeUnit timeUnit) {
        return format(time, timeUnit, DATE_TIME_FORMATTER);
    }

    /**
     * 按格式输出一个日期时间
     * @param time      时间戳
     * @param timeUnit  时间单位
     * @param formatter 输出格式
     * @return 日期时间
     */
    public static String format(long time, TimeUnit timeUnit, DateTimeFormatter formatter) {
        return format(time, timeUnit, formatter, DEFAULT_ZONE_ID);
    }

    /**
     * 按格式输出一个日期时间
     * @param time      时间戳
     * @param timeUnit  时间单位
     * @param formatter 输出格式
     * @param zoneId    地区id
     * @return 日期时间
     */
    public static String format(long time, TimeUnit timeUnit, DateTimeFormatter formatter, ZoneId zoneId) {
        final long sec = timeUnit.toSeconds(time);
        return formatter.format(ZonedDateTime.ofInstant(Instant.ofEpochSecond(sec), zoneId));
    }

    /**
     * 判断一个日期时间字符串是否符合格式
     * @param formatter 格式
     * @param value     待判断字符串
     */
    public static boolean matchFormatter(String value, String formatter) {
        final DateTimeFormatter pattern = DateTimeFormatter.ofPattern(formatter);
        try {
            ZonedDateTime.parse(value, pattern);
            return true;
        } catch (Exception ignored) {
        }
        try {
            LocalDateTime.parse(value, pattern);
            return true;

        } catch (Exception ignored) {
        }
        try {
            LocalDate.parse(value, pattern);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 智能识别字符串到 ZonedDateTime
     * @param string 字符串
     * @return ZonedDateTime
     */
    public static ZonedDateTime parse(String string) {
        return parse(string, DEFAULT_ZONE_ID);
    }

    /**
     * 智能识别字符串到 ZonedDateTime
     * @param string 字符串
     * @return ZonedDateTime
     */
    public static ZonedDateTime parse(String string, ZoneId zoneId) {
        try {
            final LocalDateTime parse = LocalDateTime.parse(string, FULL_FORMATTER);
            return ZonedDateTime.ofLocal(parse, zoneId, null);
        } catch (Exception ignored) {
        }
        try {
            final LocalDateTime parse = LocalDateTime.parse(string, DATE_TIME_FORMATTER);
            return ZonedDateTime.ofLocal(parse, zoneId, null);
        } catch (Exception ignored) {
        }
         try {
            final LocalDateTime parse = LocalDateTime.parse(string, DATE_TIME_FORMATTER_2);
            return ZonedDateTime.ofLocal(parse, zoneId, null);
        } catch (Exception ignored) {
        }
        try {
            final LocalDate parse = LocalDate.parse(string, DATE_FORMATTER);
            return ZonedDateTime.of(parse, LocalTime.MIN, zoneId);
        } catch (Exception ignored) {
        }
        try {
            final LocalDate parse = LocalDate.parse(string, MONTH_FORMATTER);
            return ZonedDateTime.of(parse, LocalTime.MIN, zoneId);
        } catch (Exception ignored) {
        }
        try {
            return ZonedDateTime.parse(string);
        } catch (Exception ignored) {
        }

        throw new RuntimeException("无法识别的日期格式:" + string);
    }

    /**
     * 返回简单格式的时间间隔
     * @param start 开始时刻(毫秒)
     * @param end   结束时刻(毫秒)
     * @return 简单格式时间间隔
     */
    @SuppressWarnings("AlibabaUndefineMagicConstant")
    public static String simpleDuration(long start, long end) {
        final long ms = end - start;
        final long seconds = MILLISECONDS.toSeconds(ms);
        final long minutes = SECONDS.toMinutes(seconds);
        //10秒内显示毫秒
        if (ms < SECONDS.toMillis(10)) {
            return ms + "ms";
        }
        //2分钟内显示秒
        if (ms < MINUTES.toMillis(2L)) {
            return seconds + "s";
        }
        // 10分钟内显示 分+秒
        if (ms < MINUTES.toMillis(10)) {
            final long s = seconds - MINUTES.toSeconds(minutes);
            return String.format("%dm%ds", minutes, s);
        }
        // 1小时内显示 分钟
        if (ms < HOURS.toMillis(1)) {
            return SECONDS.toMinutes(seconds) + "m";
        }
        // 2天内 显示 小时+分
        if (ms < DAYS.toMillis(2)) {
            final long h = MINUTES.toHours(minutes);
            final long m = minutes - HOURS.toMinutes(h);
            return String.format("%dh%dm", h, m);
        }
        // 超过2天 显示 天+小时+分
        final long d = MINUTES.toDays(minutes);
        final long h = MINUTES.toHours(minutes - DAYS.toMinutes(d));
        final long m = minutes - DAYS.toMinutes(d) - HOURS.toMinutes(h);
        return String.format("%dd%dh%dm", d, h, m);
    }
}   
