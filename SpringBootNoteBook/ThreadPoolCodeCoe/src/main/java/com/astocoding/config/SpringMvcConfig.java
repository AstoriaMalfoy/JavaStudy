package com.astocoding.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.concurrent.Executor;

@Configuration
@EnableWebMvc
public class SpringMvcConfig implements AsyncConfigurer {


    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Override
    public Executor getAsyncExecutor() {
        return threadPoolTaskExecutor;
    }
}
