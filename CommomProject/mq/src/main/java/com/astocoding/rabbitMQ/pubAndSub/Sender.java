package com.astocoding.rabbitMQ.pubAndSub;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/10 16:14
 */
public class Sender {
    public static String QUEUE_NAME = "PUB_AND_SUB_QUEUE";

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String[] args) {
        executorService.submit(() -> sender(QUEUE_NAME, "hello world ......."));
        executorService.submit(() -> sender(QUEUE_NAME, "hello world ......."));
    }


    @SneakyThrows
    public static void sender(String queueName, String message) {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("localhost");
        try (Connection connection = connectionFactory.newConnection()){
            Channel channel = connection.createChannel();
            channel.queueDeclare(queueName, false, false, false, null);
            channel.exchangeDeclare("logs", "fanout");
            channel.basicPublish("logs", "", null, message.getBytes());
        }
    }
}

