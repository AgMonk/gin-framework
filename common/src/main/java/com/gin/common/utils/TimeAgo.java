package com.gin.common.utils;

import com.gin.jackson.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 返回指定时间戳离当前时间的时长表述
 *
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2023/8/8 15:33
 **/
public class TimeAgo {
    /**
     * 默认条件
     */
    public static final List<Condition> DEFAULT_CONDITIONS = new ArrayList<>();

    static {
        DEFAULT_CONDITIONS.add(new Condition(1, TimeUnit.MINUTES, d -> "刚刚"));
        DEFAULT_CONDITIONS.add(new Condition(1, TimeUnit.HOURS, d -> TimeUnit.SECONDS.toMinutes(d) + "分钟前"));
        DEFAULT_CONDITIONS.add(new Condition(2, TimeUnit.DAYS, d -> TimeUnit.SECONDS.toHours(d) + "小时前"));
        DEFAULT_CONDITIONS.add(new Condition(30, TimeUnit.DAYS, d -> TimeUnit.SECONDS.toDays(d) + "天前"));
    }

    /**
     * 根据给出的判定条件，返回指定时间戳与当前时间的时长表述
     *
     * @param timestamp 时间戳(秒)
     * @return 时长表述
     */
    public static String ago(long timestamp) {
        return ago(timestamp, DEFAULT_CONDITIONS);
    }

    /**
     * 根据给出的判定条件，返回指定时间戳与当前时间的时长表述
     *
     * @param timestamp  时间戳(秒)
     * @param conditions 判定条件
     * @return 时长表述
     */
    public static String ago(long timestamp, List<Condition> conditions) {
        if (ObjectUtils.isEmpty(conditions)) {
            throw new RuntimeException("条件为空");
        }
        final long duration = System.currentTimeMillis() / 1000 - timestamp;
        for (Condition condition : conditions) {
            if (duration < condition.unit.toSeconds(condition.time)) {
                return condition.formatter.format(duration);
            }
        }
        return conditions.get(conditions.size() - 1).formatter.format(duration);
    }

    /**
     * 根据给出的判定条件，返回指定时间戳与当前时间的时长表述
     *
     * @param zonedDateTime 时间戳(秒)
     * @return 时长表述
     */
    public static String ago(ZonedDateTime zonedDateTime) {
        return ago(zonedDateTime, DEFAULT_CONDITIONS);
    }

    /**
     * 根据给出的判定条件，返回指定时间戳与当前时间的时长表述
     *
     * @param zonedDateTime 时间戳(秒)
     * @param conditions    判定条件
     * @return 时长表述
     */
    public static String ago(ZonedDateTime zonedDateTime, List<Condition> conditions) {
        return ago(zonedDateTime.toEpochSecond(), conditions);
    }

    /**
     * 条件
     */
    @RequiredArgsConstructor
    public static class Condition {
        final long time;
        final TimeUnit unit;
        final Formatter formatter;
    }

    public interface Formatter {
        /**
         * 将间隔(秒)转换成指定格式
         *
         * @param duration 间隔(秒)
         * @return 时长表述
         */
        String format(long duration);
    }
}
