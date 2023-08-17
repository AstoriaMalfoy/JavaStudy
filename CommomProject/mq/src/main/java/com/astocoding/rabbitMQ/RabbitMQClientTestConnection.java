package com.astocoding.rabbitMQ;

import com.astocoding.rabbitMQ.constant.Config;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/1 11:16
 */
public class RabbitMQClientTestConnection {


    private String host;
    private int port;
    private String userName;
    private String passWord;



    public RabbitMQClientTestConnection(String host, int port, String userName, String passWord) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.passWord = passWord;
    }


    /**
     *  连接工厂
     */
    private ConnectionFactory connectionFactory;

    private Connection connection;

    private Channel channel;
    /**
     * 1.创建连接工厂
     */
    private void initConnectionFactory(){
        this.connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(userName);
        connectionFactory.setPassword(passWord);
    }

    /**
     * 2.创建连接
     */
    private void initConnection(){
        try {
            this.connection = this.connectionFactory.newConnection();
        } catch (IOException | TimeoutException e) {
            System.out.println("创建连接失败");
            throw new RuntimeException(e);
        }
    }


    /**
     * 3.创建信道并且声明队列
     */
    private void initChannel(){
        try {
            this.channel = connection.createChannel();
            channel.queueDeclare(Config.TEST_QUEUE_A,false,false,false,null);
        } catch (IOException e) {
            System.out.println("创建信道失败");
            throw new RuntimeException(e);
        }

    }


    public void init(){
        initConnectionFactory();
        initConnection();
        initChannel();
    }

    public void sendMessage(String message,String topic){
        try {
            channel.basicPublish("",topic,null,message.getBytes());
            System.out.println("发送消息成功" + message);
        } catch (IOException e) {
            System.out.println("发送消息失败" + message);
            throw new RuntimeException(e);
        }
    }

    public void receiveMessage(String topic){
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println(topic + " 收到消息：" + new String(body));
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        try {
            channel.basicConsume(topic,true,consumer);
        } catch (IOException e) {
            System.out.println("消费消息失败" + topic);
        }
    }


    public static void main(String[] args) throws InterruptedException {

        ScheduledExecutorService scheduledExecutorService  = Executors.newScheduledThreadPool(10);

        /**
         * 创建发送客户
         */
        RabbitMQClientTestConnection rabbitSender = new RabbitMQClientTestConnection(
                Config.RABBITMQ_HOST,Config.RABBITMQ_1_PORT,Config.RABBITMQ_USERNAME,Config.RABBITMQ_PASSWORD
        );

        /**
         * 创建接收客户端
         */
        RabbitMQClientTestConnection rabbitConsumer = new RabbitMQClientTestConnection(
                Config.RABBITMQ_HOST,Config.RABBITMQ_1_PORT,Config.RABBITMQ_USERNAME,Config.RABBITMQ_USERNAME
        );
        rabbitSender.init();
        rabbitConsumer.init();

        // 延迟发送
        scheduledExecutorService.scheduleWithFixedDelay(
                () -> rabbitSender.sendMessage("hello world " + System.currentTimeMillis(),"test_queue_a"),
                1,
                100,
                TimeUnit.MILLISECONDS
        );

        // 注册接收
        rabbitConsumer.receiveMessage("test_queue_a");

        Thread.sleep(1000 * 60 * 10);

    }
}
