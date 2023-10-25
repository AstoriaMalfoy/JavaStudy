package com.astocoding;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/22 10:39
 */
public class AlignSecondTest {
    public static void main(String[] args) throws InterruptedException {

        ConcurrentMap<Integer, Integer> missCount = new ConcurrentHashMap<>();


        for (int i = 0; i < 100; i++) {

            new Thread(() -> {
                System.out.println("the thread is " + Thread.currentThread().getName() + " start .. ");
                System.out.println("Hello world");
                for (int a = 0; a < 10; a++) {
                    try {
                        Thread.sleep(1000 - System.currentTimeMillis() % 1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    Long miss = System.currentTimeMillis() % 1000;
                    if (Objects.isNull(missCount.get(miss.intValue()))) {
                        missCount.putIfAbsent(miss.intValue(), 1);
                    } else {
                        missCount.put(miss.intValue(), missCount.get(miss.intValue()) + 1);
                    }
                }
            },"the thread " + i ).start();
        }

        Thread.sleep(1000 * 15);
        
        for (Integer integer : missCount.keySet()) {
            System.out.println(integer + " : " + missCount.get(integer));
        }
    }
}
