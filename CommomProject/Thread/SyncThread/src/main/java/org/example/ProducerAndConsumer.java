package org.example;

import lombok.SneakyThrows;

import java.util.concurrent.CountDownLatch;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/14 15:50
 */
public class ProducerAndConsumer {

    private Integer resource = 0;

    private Integer producerLock = 1;
    private Integer consumerLock = 1;


    private CountDownLatch countDownLatch = new CountDownLatch(2);

    @SneakyThrows
    public static void main(String[] args) {
        ProducerAndConsumer producerAndConsumer = new ProducerAndConsumer();
        producerAndConsumer.startProducer(10);
        producerAndConsumer.startConsumer(10);
        producerAndConsumer.countDownLatch.await();
        System.out.println("Start one producer");
        synchronized (producerAndConsumer.producerLock) {
            System.out.println("Start one producer");
            producerAndConsumer.producerLock.notify();
        }
    }


    private void startProducer(Integer producerCount) {
        for (int i = 0; i < producerCount; i++) {
            new Thread(() -> {
                while (!Thread.interrupted()) {
                    synchronized (producerLock) {
                        synchronized (consumerLock) {
                            try {
                                producerLock.wait();
                                resource = resource + 1;
                                System.out.println("[" + Thread.currentThread().getName() + "]" + "producer - set - resource" + resource);
                                Thread.sleep(100);
                                System.out.println("[" + Thread.currentThread().getName() + "]" + "start a consumer");
                                consumerLock.notify();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
    }

    private void startConsumer(Integer consumerCount) {
        for (int i = 0; i < consumerCount; i++) {
            new Thread(() -> {
                while (!Thread.interrupted()) {
                    synchronized (producerLock) {
                        synchronized (consumerLock) {
                            try {
                                consumerLock.wait();
                                System.out.println("[" + Thread.currentThread().getName() + "]" + "consumer-get-resource " + resource);
                                Thread.sleep(100);
                                System.out.println("[" + Thread.currentThread().getName() + "]" + "start a producer");
                                producerLock.notify();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
    }

}

