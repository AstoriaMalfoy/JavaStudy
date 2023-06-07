package com.ascoding;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/21 16:00
 */
public class CLH {

    /**
     * 尾节点
     */
    private AtomicReference<CLHNode> tailNode;

    /**
     * 线程绑定的lock
     * preNode 用于判断当前线程是否可以执行 -- 获得锁
     * currentNode : 当前线程执行完毕之后 -- 释放锁
     */
    private ThreadLocal<CLHNode> tlPreNode = new ThreadLocal<>();
    private ThreadLocal<CLHNode> tlCurrentNode = new ThreadLocal<>();

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CLHNode {
        // 代表当前节点持有线程的状态 true-当前节点已经持有锁，false-当前节点还没持有锁
        public volatile boolean hasLock = false;
        public void setHasLock(boolean b){
            this.hasLock = b;
        }
    }

    public CLH() {
        // init clh
        CLHNode clhNode = new CLHNode();
        clhNode.setHasLock(true);
        this.tailNode = new AtomicReference<>(clhNode);
    }

    public void lock() {
        CLHNode tempPreNode = null;
        CLHNode tempCurrentNode = null;
        do {
            tlCurrentNode.set(tempCurrentNode = new CLHNode());
            tlPreNode.set(tempPreNode = tailNode.get());
        } while (!tailNode.compareAndSet(tempPreNode, tempCurrentNode));

        while (!tempPreNode.hasLock){
//            try {
//                System.out.println("the thread " + Thread.currentThread().getName() + " wait for the lock...");
//                Thread.sleep(200);
//            } catch (InterruptedException e) {
//            }
        }
        System.out.println("the thread " + Thread.currentThread().getName() + " get lock ...");
    }

    public void unlock() {
        CLHNode clhNode = tlCurrentNode.get();
        clhNode.hasLock = true;
        System.out.println("the thread " + Thread.currentThread().getName() + " release get lock .. ");
    }

    public static void main(String[] args) {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(20, new ThreadFactory() {
            private int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"clh-thread" + count++);
            }
        });
        CLH clhLock = new CLH();
        BoxObject<Integer> tCount = new BoxObject<>(20);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i=0;i<threadCount;i++){
            executorService.execute(()->{
                try {
                    // wait for together start
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clhLock.lock();
                Integer currentThreadGetIndex = tCount.getObj();
                tCount.setObj(currentThreadGetIndex-1);
                System.out.println("the thread " + Thread.currentThread().getName() + " get count is " + currentThreadGetIndex + " ...");
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                clhLock.unlock();


                // try redo lock

                clhLock.lock();
                currentThreadGetIndex = tCount.getObj();
                tCount.setObj(currentThreadGetIndex-1);
                System.out.println("the thread " + Thread.currentThread().getName() + " get count is " + currentThreadGetIndex + " ...");
//                try {
//                    Thread.sleep(500);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                clhLock.unlock();

            });
        }
        countDownLatch.countDown();
    }

    public static class BoxObject<T>{
        private T obj;
        public BoxObject (T obj){
            this.obj = obj;
        }
        public void setObj(T obj){
            this.obj = obj;
        }
        public T getObj(){
            return this.obj;
        }
    }
}
