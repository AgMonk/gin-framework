package com.gin.common.utils;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池工具类
 * @author : ginstone
 * @version : v1.0.0
 * @since : 2022/12/27 14:34
 */
public class ThreadPoolUtils {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        public ThreadPoolTaskExecutor build() {
            executor.initialize();
            return executor;
        }

        /**
         * 使用默认配置
         * @return Builder
         */
        public Builder defaultConfig() {
            return this
                    //核心线程池大小
                    .setCorePoolSize(10)
                    //最大线程数
                    .setMaxPoolSize(10)
                    //队列容量
                    .setQueueCapacity(1000)
                    //活跃时间
                    .setKeepAliveSeconds(30)
                    //线程名字前缀
                    .setThreadNamePrefix("executor-")
                    // CallerRunsPolicy：不在新线程中执行任务，而是由调用者所在的线程来执行
                    .setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy())
                    // 等待所有任务结束后再关闭线程池
                    .setWaitForTasksToCompleteOnShutdown(true)
                    ;
        }

        /**
         * 核心线程池大小
         * @param corePoolSize 核心线程池大小
         * @return Builder
         */
        public Builder setCorePoolSize(int corePoolSize) {
            executor.setCorePoolSize(corePoolSize);
            return this;
        }

        /**
         * 活跃时间
         * @param keepAliveSeconds 活跃时间
         * @return Builder
         */
        public Builder setKeepAliveSeconds(int keepAliveSeconds) {
            executor.setKeepAliveSeconds(keepAliveSeconds);
            return this;
        }

        /**
         * 最大线程数
         * @param maxPoolSize 最大线程数
         * @return Builder
         */
        public Builder setMaxPoolSize(int maxPoolSize) {
            executor.setMaxPoolSize(maxPoolSize);
            return this;
        }

        /**
         * 队列容量
         * @param queueCapacity 队列容量
         * @return Builder
         */
        public Builder setQueueCapacity(int queueCapacity) {
            executor.setQueueCapacity(queueCapacity);
            return this;
        }

        /**
         * pool已经达到max size的时候，如何处理新任务
         * @param rejectedExecutionHandler handler
         * @return Builder
         */
        public Builder setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
            executor.setRejectedExecutionHandler(rejectedExecutionHandler);
            return this;
        }

        /**
         * 线程名字前缀
         * @param threadNamePrefix 前缀
         * @return Builder
         */
        public Builder setThreadNamePrefix(String threadNamePrefix) {
            executor.setThreadNamePrefix(threadNamePrefix);
            return this;
        }

        /**
         * 等待所有任务结束后再关闭线程池
         * @param waitForJobsToCompleteOnShutdown 等待所有任务结束后再关闭线程池
         * @return Builder
         */
        public Builder setWaitForTasksToCompleteOnShutdown(boolean waitForJobsToCompleteOnShutdown) {
            executor.setWaitForTasksToCompleteOnShutdown(waitForJobsToCompleteOnShutdown);
            return this;
        }
    }
}   
