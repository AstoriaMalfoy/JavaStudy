package com.astocoding.controller;

import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class DemoInterface {


    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @GetMapping("inta")
    @SneakyThrows
    public String interfaceA(String str){
        String currentThreadName = Thread.currentThread().getName();
        log.info("invoke thread inta : " + currentThreadName);
        Thread.sleep(1000);
        return str;
    }

    @GetMapping("intb")
    @SneakyThrows
    public String interfaceB(int count){
        String currentThreadName = Thread.currentThread().getName();
        log.info("invoke thread intb : " + currentThreadName);
        return String.valueOf(count);
    }



}
