package com.astocoding.rabbitMQ.constant;

import com.rabbitmq.client.Connection;
import lombok.Data;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/1 11:33
 */
@Data
public class Config {

    public static final String RABBITMQ_HOST = "127.0.0.1";

    public static final int RABBITMQ_1_PORT = 5673;

    public static final int RABBITMQ_2_PORT = 5674;

    public static final int RABBITMQ_3_PORT = 5675;

    public static final String RABBITMQ_USERNAME = "guest";

    public static final String RABBITMQ_PASSWORD = "guest";

    public static final String TEST_QUEUE_A = "test_queue_a";

    private Config(){}

}