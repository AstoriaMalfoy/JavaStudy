package com.astocoding;

import lombok.SneakyThrows;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

public class FutureDemo implements Callable<String> {
    @Override
    public String call() throws Exception {
        return "hello";
    }

    @SneakyThrows
    public static void main(String[] args) {
        FutureTask<String> futureTask = new FutureTask<>(new FutureDemo());
        Thread thread = new Thread(futureTask);
        thread.start();
        String s = futureTask.get();
        System.out.println(s);
    }
}
