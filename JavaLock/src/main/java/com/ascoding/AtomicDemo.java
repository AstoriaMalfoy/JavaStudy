package com.ascoding;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/16 16:38
 */
public class AtomicDemo {


    Executor executor = Executors.newFixedThreadPool(10);

    AtomicBoolean atomicBoolean;
    AtomicInteger atomicInteger;
    AtomicLong atomicLong;

    public static void main(String[] args) throws InterruptedException {
        new AtomicDemo().testAtomIntgerSec();
    }

    public void testAtomIntgerSec() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i=0;i<10;i++){
            executor.execute(()->{
                int result = atomicInteger.get();
                result-=1;
                atomicInteger.set(result);
                System.out.println("the update result is " + result);
//                boolean b = atomicInteger.compareAndSet(result, result - 1);
//                System.out.println("thread " + Thread.currentThread().getName() + " update result " + b + " get result is " + result);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println(atomicInteger.get());
    }



}
