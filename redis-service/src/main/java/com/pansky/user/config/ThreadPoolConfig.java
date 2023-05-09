package com.pansky.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author Fo
 * @date 2022/11/12 21:15
 */
@Configuration
@EnableAsync    // 允许使用异步线程, 与@Async配合使用，用于执行异步任务
public class ThreadPoolConfig {
    // 核心线程数
    private int corePoolSize = 6;
    // 最大线程数
    private int maxPoolSize = 10;
    // 队列大小
    private int queueSize = 100;
    // 线程最大空闲时间
    private int keepAliveSeconds = 100;
    /**
     * 自定义消费队列线程池
     * CallerRunsPolicy 这个策略重试添加当前的任务，他会自动重复调用 execute() 方法，直到成功。
     * AbortPolicy 对拒绝任务抛弃处理，并且抛出异常。
     * DiscardPolicy 对拒绝任务直接无声抛弃，没有异常信息。
     * DiscardOldestPolicy 对拒绝任务不抛弃，而是抛弃队列里面等待最久的一个线程，然后把拒绝任务加到队列。
     * @return
     */
    @Bean(value = "threadPool")
    public ThreadPoolTaskExecutor buildFirstThreadPool() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueSize);
        executor.setThreadNamePrefix("myThreadPool-");
        executor.setKeepAliveSeconds(keepAliveSeconds);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //如果@Bean 就不需手动，会自动InitializingBean的afterPropertiesSet来调initialize
//      executor.initialize();
        return executor;
    }

}

