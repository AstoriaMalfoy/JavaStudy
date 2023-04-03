package com.astocoding;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/24 15:23
 */
public class InterruptedDemo {


    public static void main(String[] args) {
        InterruptedDemo interruptedDemo = new InterruptedDemo();
//        interruptedDemo.runningThreadTestInterrupted();
//        interruptedDemo.waitOrTimeWaitThreadTestInterrupted();
//        interruptedDemo.blockThreadTestInterrupted();
//        interruptedDemo.initOrTerminate();
        // test join
        interruptedDemo.testManyJoin();
    }





    /**
     * 线程中断测试-运行状态
     * 处于运行中的线程需要手动的获取中断标志位，然后显示的进行处理
     * 如果没有接受到中断信号，那么线程会一直运行下去
     * 中断信号对实际运行中的线程没有任何影响，相当于只是设置了线程中的一个变量而已
     */
    public void runningThreadTestInterrupted() {
        Thread thread = new Thread(() -> {
            while (true) {
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("线程被中断了");
                    break;
                }
                System.out.println("线程正在运行");
            }
        });
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }


    /**
     *  处于 WAIT / TIME_WAIT 的状态
     *  如果线程处于 wait 或者 time_wait 的状态，此时如果调用 interrupt方法，会触发 interruptedException 异常
     *  线程在调用以下函数的时候会处于 WAIT 状态
     *  public final void join() throws InterruptedException;
     *  public final void wait() throws InterruptedException;
     *
     *  线程调用以下函数会处于 TIME_WAIT 状态
     *  public final native void wait(long timeout) throws InterruptedException;
     *  public static native void sleep(long millis) throws InterruptedException;
     *  public final synchronized void join(long millis) throws InterruptedException;
     *
     *  不难发现，这些所有函数都抛出了 InterruptedException 并且是一个受检异常，也就是线程必须对这个异常做出响应。
     *  在触发异常之后，线程的 interruptedException 状态会被设置为false 代表线程已经对中断做出了相应。
     */
    public void waitOrTimeWaitThreadTestInterrupted(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(isInterrupted());
                    e.printStackTrace();
                }
                System.out.println("finish sleep : sleep for " + (System.currentTimeMillis() - startTime) + " ms");
            }
        };
        thread.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread.interrupt();
    }


    /**
     *  线程在处于 block 状态时，是不会相应中断信息的，但是中断标志位还是会设置。
     *  当线程标志位被设置成true的时候，还是可以参与到锁的竞争中的，也就是 处于 BLOCK 状态的线程 接收到中断信号 结束 BLOCK 阶段之后还可以继续进入到 BLOCK 阶段
     *  但是如果处于 BLOCK 状态的线程，接收到中断信号之后，如果还想进入到 WAIT / TIME_WAIT 状态，依旧会抛出异常。
     *  在使用 synchronized 的时候不响应中断信号，这是 synchronized 的局限性，如果在被锁阻塞的时候想要相应中断，应该使用Java提供的可以响应中断的锁
     */
    public void blockThreadTestInterrupted(){
        Object lock = new Object();
        Object lock2 = new Object();
        Thread thread = new Thread(){
            @Override
            public void run() {
                synchronized (lock){
                    System.out.println("线程获取锁 lock 1。。。。。 interrupted :" + isInterrupted());
                    synchronized(lock2){
                        System.out.println("线程获取锁 lock 2。。。。。 interrupted :" + isInterrupted());
                    }
                }
            }
        };

        Thread thread1 = new Thread(){
            @Override
            public void run() {
                synchronized (lock2){
                    // sleep 3000
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        thread1.start();

        synchronized (lock){
            thread.start();
            thread.interrupt();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



    public void testManyJoin(){


        Thread threadA = new Thread(){
            @Override
            public void run() {
                System.out.println("the thread A is running ....");
                // sleep 3000
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };


        // countDownLatch for  threadB and threadC
        CountDownLatch countDownLatch = new CountDownLatch(2);


        Thread threadB = new Thread(() -> {
            // wait countDownLatch
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("the thread is running ....");
            // join threadA
            try {
                threadA.join();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Thread threadC = new Thread(() -> {
            // wait countDownLatch
            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("the thread is running ....");
            // join threadA
            try {
                threadA.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        // start threadA and threadB and threadC
        threadA.start();
        threadB.start();
        threadC.start();

        // countDown countDownLatch
        countDownLatch.countDown();
        countDownLatch.countDown();

        // while threadA threadB threadC is running , print threadA threadB threadC state in one line
        while (threadA.isAlive() || threadB.isAlive() || threadC.isAlive()){
            System.out.println("threadA state : " + threadA.getState() + " threadB state : " + threadB.getState() + " threadC state : " + threadC.getState());
        }

    }


    /**
     * 处于初始化状态或者结束状态的线程不会响应任何中断信息，也不会设置中断位置
     */
    public void initOrTerminate(){
        Thread thread = new Thread(){
            @Override
            public void run() {
                System.out.println("the thread is running ....");
            }
        };

        thread.interrupt();
        System.out.println(thread.isInterrupted());
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(thread.isInterrupted());
        thread.interrupt();
    }
}
