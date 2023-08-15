package com.astocoding.rocketMQ.helloworld;

import lombok.SneakyThrows;
import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.java.route.TopicRouteData;
import org.apache.rocketmq.shaded.com.google.gson.annotations.SerializedName;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/11 16:43
 */
public class Sender {

    private static final String NameSrvAddress = "localhost:9876";

    private static final String TOPCI_NAME = "htllo-world-TopicTest";

    @SneakyThrows
    public static void main(String[] args)  {
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        ClientConfiguration clientConfiguration = ClientConfiguration.newBuilder().setEndpoints(NameSrvAddress).build();
        Producer producer = provider.newProducerBuilder()
                .setTopics(TOPCI_NAME)
                .setClientConfiguration(clientConfiguration)
                .build();
        provider.newMessageBuilder()
                .setTopic(TOPCI_NAME)
                .setBody("hello world".getBytes())
                .setTag("hello tag")
                .build();

    }
}
