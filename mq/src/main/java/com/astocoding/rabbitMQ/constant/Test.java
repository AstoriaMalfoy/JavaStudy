package com.astocoding.rabbitMQ.constant;

import lombok.SneakyThrows;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 1小时，java实现
 * 生产消费模式：
 * 1、一个线程模拟生产者，每100ms生产10个元素的字符串加入到ArrayList中，并通知消费者线程消费；
 * 2、两个线程模拟消费者，从ArrayList每次消费一个元素的字符串，将字符串转换为大写以后，重新放入ArrayList队首；
 * 3、消费者线程不能重复处理同一个元素，如果程序没有终止，且ArrayList没有新加入的值，则消费者线程处于等待ArrayList加入新值状态；
 * 4、ArrayList是非线程安全的集合，操作ArrayList应考虑加锁；
 * 5、如果ArrayList中的字符串个数超过100个，程序终止；
 **/

public class Test {

    final List<String> resource = new ArrayList(100);

    // 生产者生产offset
    volatile Integer producerOffset = 0;
    // 消费者消费offset
    volatile Integer consumerOffet = 0;
    // 通知消费锁
    final Object notifyConsumer = "xx";

    CountDownLatch countDownLatch = new CountDownLatch(3);

    public static void main(String[] args) {
        Test test = new Test();
        // 生产者线程
        new Thread(test::producer).start();
        // 消费者线程 * 2
        new Thread(test::consumer).start();
        new Thread(test::consumer).start();
        try {
            test.countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(test.resource);

    }

    private void producer() {
        while (producerOffset < 100) {
            try {
                synchronized (notifyConsumer) {
                    resource.add("text " + producerOffset);
                    System.out.printf("生产者生产：%s \n", "text " + producerOffset);
                    // 生产者offset 增加
                    producerOffset++;
                    // 唤醒可能存在的消费者
                    notifyConsumer.notifyAll();
                    // 速率控制
                    Thread.sleep(10);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        countDownLatch.countDown();
    }

    @SneakyThrows
    private void consumer() {
        while (true) {
            synchronized (notifyConsumer) {
                // 消费超过 100 ， 停止消费
                if (consumerOffet >= 99) {
                    countDownLatch.countDown();
                    return;
                }
                // 如果消费进度滞后，需要进行消费
                if (consumerOffet < producerOffset) {
                    String str = resource.get(consumerOffet);
                    System.out.println("消费者消费：" + str);
                    resource.remove(str);
                    resource.add(0, str.toUpperCase());
                    // 移动消费进度
                    consumerOffet++;
                } else {
                    // 消费无滞后，等待生产者生产
                    notifyConsumer.wait();
                }
            }
        }
    }


}