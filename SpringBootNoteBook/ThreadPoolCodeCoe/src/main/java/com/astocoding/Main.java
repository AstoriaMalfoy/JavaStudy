package com.astocoding;

import cn.hippo4j.core.enable.EnableDynamicThreadPool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
@EnableDynamicThreadPool
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class);
        log.info("the application start successful.............");
    }
}