package com.astocoding.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadFactory;

@Slf4j
@Configuration
public class BeanConfig {
    @Bean
    public ThreadPoolTaskExecutor taskExecutor(){
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
        taskExecutor.setCorePoolSize(10);
        taskExecutor.setMaxPoolSize(10);
        taskExecutor.setQueueCapacity(0);
        taskExecutor.setThreadFactory((ThreadFactory) r -> {
            Thread thread = new Thread(r);
            thread.setName("stuart-threadPool-thread");
            return thread;
        });
        return taskExecutor;
    }
}
