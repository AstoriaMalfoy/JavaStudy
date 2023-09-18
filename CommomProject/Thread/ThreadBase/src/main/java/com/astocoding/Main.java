package com.astocoding;

import lombok.SneakyThrows;
import org.junit.Test;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        getString();
    }

    public String getString() {
        return "this is a test Stirng ";
    }

    @Test
    @SneakyThrows
    public void testRunOneSecond() {
        for (int i = 0; i < 100; i++) {
            Thread thread = new Thread(this::run);
            thread.setName("TestThread" + i);
            thread.start();
            Thread.sleep(1000);
            thread.interrupt();
            Thread.sleep(1000);
        }
    }

    private void run() {
        long startTime = System.nanoTime();
        while (Thread.interrupted()) {
        }
        long endTime = System.nanoTime();
        System.out.println(Thread.currentThread().getName() +  "  Time: " + (endTime - startTime) + "ns");
    }
}
