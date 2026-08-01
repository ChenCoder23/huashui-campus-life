package com.huashui.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;


@Configuration
public class ThreadPoolConfig {

    /**
     * 通用异步任务线程池
     * 适用场景：发邮件、写日志、推送通知等非核心业务
     */
    @Bean("taskExecutor")
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(4);                             // 核心线程数
        executor.setMaxPoolSize(8);                              // 最大线程数
        executor.setQueueCapacity(100);                          // 队列容量
        executor.setKeepAliveSeconds(60);                        // 空闲线程存活时间
        executor.setThreadNamePrefix("async-task-");             // 线程名前缀（排查问题时很重要！）
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()        // 拒绝策略
        );
        executor.setWaitForTasksToCompleteOnShutdown(true);      // 优雅关闭：等任务执行完
        executor.setAwaitTerminationSeconds(30);                 // 最多等 30 秒

        executor.initialize();
        return executor;
    }

    /**
     * 计算密集型任务线程池
     * 适用场景：CPU 密集型计算（数据分析、复杂计算）
     * 核心线程数 = CPU 核数 + 1
     */
    @Bean("computeExecutor")
    public ThreadPoolTaskExecutor computeExecutor() {
        int cpuCores = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cpuCores + 1);
        executor.setMaxPoolSize(cpuCores + 1);
        executor.setQueueCapacity(200);
        executor.setThreadNamePrefix("compute-");
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.AbortPolicy()             // 计算任务满了直接拒绝
        );
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }

    /**
     * IO 密集型任务线程池
     * 适用场景：大量 IO 操作（数据库查询、RPC 调用、文件读写）
     * 线程数可以设大一些 = CPU 核数 * 2
     */
    @Bean("ioExecutor")
    public ThreadPoolTaskExecutor ioExecutor() {
        int cpuCores = Runtime.getRuntime().availableProcessors();

        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(cpuCores * 2);
        executor.setMaxPoolSize(cpuCores * 4);
        executor.setQueueCapacity(500);
        executor.setKeepAliveSeconds(120);
        executor.setThreadNamePrefix("io-task-");
        executor.setRejectedExecutionHandler(
                new ThreadPoolExecutor.CallerRunsPolicy()
        );
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.initialize();
        return executor;
    }
}
