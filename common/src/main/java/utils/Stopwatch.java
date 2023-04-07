package utils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;

import static utils.TimeUtils.simpleDuration;


/**
 * 秒表
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/23 11:02
 */
@Getter
@Slf4j
public class Stopwatch {
    private static final String TEMPLATE = "[{}] 标记计时点 [{}] 距离上一个计时点: {} 总耗时: {}";
    /**
     * 任务名称
     */
    private final String taskName;
    /**
     * 计时开始的时间
     */
    private final long start;
    /**
     * 计时过程
     */
    private final LinkedHashMap<String, Long> process = new LinkedHashMap<>();
    /**
     * 上一个标签计时点
     */
    private long last;

    public Stopwatch(String taskName) {
        this.taskName = taskName;
        final long now = now();
        this.start = now;
        this.last = now;

        log.info("[{}] 开始计时", taskName);
    }

    private static long now() {
        return System.currentTimeMillis();
    }

    /**
     * 结束
     */
    public void stop() {
        tag("结束", true);
    }

    /**
     * 标记一个计时点
     * @param tag      标签名
     * @param printLog 是否输出日志
     */
    public void tag(String tag, boolean printLog) {
        final long now = now();
        if (printLog) {
            log.info(TEMPLATE, taskName, tag, simpleDuration(last, now), simpleDuration(start, now));
        }
        this.last = now;
        process.put(tag, now);
    }

    /**
     * 标记一个计时点
     * @param tag 标签名
     */
    public void tag(String tag) {
        tag(tag, false);
    }

}
