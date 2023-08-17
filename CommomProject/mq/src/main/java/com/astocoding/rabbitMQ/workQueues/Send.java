package com.astocoding.rabbitMQ.workQueues;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/10 11:38
 */
public class Send {
    public static final String QUEUE_NAME = "hello";
    public static final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        executorService.submit(() -> sender(QUEUE_NAME, "hello world ......."));
        executorService.submit(() -> sender(QUEUE_NAME, "hello world ..."));
   //     executorService.submit(() -> sender(QUEUE_NAME, "hello world .."));
   //     executorService.submit(() -> sender(QUEUE_NAME, "hello world ."));
    }

    public static void sender(String queueName, String message) {
        // 创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // 创建连接
        try (Connection connection = factory.newConnection()) {
            Channel channel = connection.createChannel();
            // 声明队列 队列一旦声明之后，就不允许在以声明的形式进行更改，如果需要更改，需要删除之后重新声明
            channel.queueDeclare(queueName, false, false, false, null);
            // 发送消息
            channel.basicPublish("", queueName, null, message.getBytes());
        } catch (IOException | TimeoutException e) {
            System.out.println("发送消息失败 error:" + e.getCause());
            throw new RuntimeException(e);
        }
    }


}

