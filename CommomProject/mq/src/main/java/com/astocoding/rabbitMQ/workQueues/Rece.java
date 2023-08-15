package com.astocoding.rabbitMQ.workQueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.SneakyThrows;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/10 11:56
 */
public class Rece {
    public static final String QUEUE_NAME = "hello";
    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        // 启动两个消费者 消费同一个队列，rabbitMQ会轮询的将消息发送给消费者
        // 消费线程关闭自动自动ACK，消费线程1消费完成之不会在手动提交ACK，而消费线程二在提交之后会进行ACK
        // 默认ACK超时时间是30分钟，如果消费线程1在30分钟内没有提交ACK，rabbitMQ会将消息重新发送给消费者
        // 在使用的时候强烈建议校验好手动ACK，如果没有校验，会占用RabbitMQ大量的内存资源，因为这些会被消费过的消息无法被删除或者被标记为已经处理过。
        
        executorService.submit(() -> worker("worker1",false));
        executorService.submit(() -> worker("worker2",true));
    }

    @SneakyThrows
    private static void worker(String threadName,boolean flag) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        // 针对于每个消费者，最多可以有多少个消息没有ACK，如果超过了这个数量，就不再向消费者发送消息
        // 如果配置改值，需要注意，如果所有的消费者都被阻塞了，那么队列有可能会被填满，这个时候就需要考虑增加消费者或者降低消费者的处理速度
        channel.basicQos(1);
        System.out.println("[*] " + threadName + " waiting for message. To exit press CTRL+C");

        // 回调函数
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.printf("[x] " + threadName + "  Received '%s'\n", message);
            try {
                doWork(message);
            } catch (Exception e) {
                System.out.println("消费消息失败");
            } finally {
                if (flag){
                    // 手动确认
                    channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                }else{
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    return;
                }
            }
            System.out.println("[x] " + threadName + " message Done");
        };

        // 消费消息
        channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> {});
    }

    /**
     * 模拟耗时操作
     */
    private static void doWork(String str) {
        for (char c : str.toCharArray()) {
            if (c == '.') {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        }
    }
}

