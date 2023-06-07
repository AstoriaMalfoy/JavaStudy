package com.ascoding;

import lombok.*;

import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicReference;
import java.util.spi.CurrencyNameProvider;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/23 11:37
 */
public class MCS {
    // 线程绑定的Node
    private ThreadLocal<MCSNode> threadCurrentNode = new ThreadLocal<>();

    // 自旋竞争 - 前一个线程持有的Node
    private AtomicReference<MCSNode> tailNode = new AtomicReference<>();

    // MCS锁概述
    // MCS锁是一种基于链表的自旋锁，它的思想是：每个线程在请求锁的时候，都会创建一个自己的节点，然后将自己的节点加入到一个全局的队列中，然后不断的自旋检查自己的前驱节点的状态，如果前驱节点释放了锁，那么当前线程就可以获得锁，否则就一直自旋等待。
    // MCS锁的优点是：每个线程在自旋的时候只检查自己前驱节点的状态，不会影响其他线程，所以不会出现像公平锁那样的饥饿现象。
    // MCS锁的缺点是：每个线程都需要在本地内存中创建一个节点，而且每个节点都需要在释放锁的时候从队列中删除，这样就会带来额外的开销。
    // MCS锁的实现
    // MCS锁的实现主要分为三个步骤：
    // 1. 创建当前线程的Node
    // 2. 将当前线程的Node绑定到当前线程
    // 3. 将当前线程的Node放入队列中
    // 4. 自旋等待前驱节点释放锁
    // 5. 释放锁




    @Data
    @Setter
    @Getter
    private static class MCSNode {
        // 指向下一个节点
        private volatile MCSNode nextNode;
        // 代表当前节点持有线程的状态 true-当前节点已经持有锁，false-当前节点还没持有锁
        private volatile boolean hasLock;


        public MCSNode(boolean hasLock) {
            this.hasLock = hasLock;
        }

        public static MCSNode of(boolean hasLock) {
            return new MCSNode(hasLock);
        }
    }

    //lock
    public void lock() {
        // 1. 创建当前线程的Node
        // 在当前线程新建，使用的是本地内存，并不会存储到远地内存中，所以自旋过程中消耗的资源较少
        MCSNode currentNode = MCSNode.of(false);
        // 2. 将当前线程的Node绑定到当前线程
        threadCurrentNode.set(currentNode);
        // 3. 将当前线程的Node放入队列中
        MCSNode preNode = tailNode.getAndSet(currentNode);
        // 4. 如果preNode不为空，说明当前线程不是第一个进入队列的线程
        if (Objects.nonNull(preNode)) {
            // 4.1 将当前线程的Node的nextNode指向preNode
            preNode.setNextNode(currentNode);
            // 4.2 将当前线程的Node的hasLock设置为true
            while (!currentNode.isHasLock()) {
                // 自旋
            }
        }

        // 存在的问题：setTailNode和setNextNode不是原子操作，可能会出现线程安全问题
    }

    // unlock
    public void unlock() {
        // 1. 获取当前线程的Node
        MCSNode currentNode = threadCurrentNode.get();
        // 2. 如果当前线程的Node的nextNode为空，说明当前线程是最后一个进入队列的线程
        if (Objects.isNull(currentNode.getNextNode())) {
            // 2.1 将当前线程的Node从队列中移除
            if (tailNode.compareAndSet(currentNode, null)) {
                // 2.2 如果移除成功，说明当前线程是最后一个进入队列的线程，直接返回
                return;
            } else {
                // 2.3 如果移除失败，说明当前线程不是最后一个进入队列的线程，需要等待
                while (Objects.isNull(currentNode.getNextNode())) {
                    // 自旋
                }
            }
        }
        // 3. 将当前线程的Node的nextNode的hasLock设置为true
        currentNode.getNextNode().setHasLock(true);
    }


    public static void main(String[] args) {
        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(20, new ThreadFactory() {
            private int count = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "clh-thread" + count++);
            }
        });
        MCS clhLock = new MCS();
        MCS.BoxObject<Integer> tCount = new MCS.BoxObject<>(20);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    // wait for together start
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                clhLock.lock();
                Integer currentThreadGetIndex = tCount.getObj();
                tCount.setObj(currentThreadGetIndex - 1);
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
                tCount.setObj(currentThreadGetIndex - 1);
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

    public static class BoxObject<T> {
        private T obj;

        public BoxObject(T obj) {
            this.obj = obj;
        }

        public void setObj(T obj) {
            this.obj = obj;
        }

        public T getObj() {
            return this.obj;
        }
    }


}
