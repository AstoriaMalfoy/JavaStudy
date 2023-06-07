package com.ascoding;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/27 15:14
 */
public class CusAQS {
    private static class Sync extends AbstractQueuedSynchronizer {
        @Override
        protected boolean tryAcquire(int arg) {
            return compareAndSetState(0, 1);
        }

        @Override
        protected boolean tryRelease(int arg) {
            return compareAndSetState(1, 0);
        }

        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

    }


    private Sync sync = new Sync();

    // lock
    public void lock() {
        sync.acquire(1);
    }

    // unlock
    public void unlock() {
        sync.release(1);
    }


    static int count = 0;

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        CusAQS cusAQS = new CusAQS();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    // loop 10000 times
                    for (int j = 0; j < 10000; j++) {
                        cusAQS.lock();
                        count++;
                        cusAQS.unlock();
                    }
                    countDownLatch.countDown();
                }
            });
        }

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("count is " + count);
    }

}
