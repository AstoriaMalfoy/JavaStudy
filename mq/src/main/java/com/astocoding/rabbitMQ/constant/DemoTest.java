package com.astocoding.rabbitMQ.constant;

public class DemoTest {
    /**
     * 生产消费模式：
     * 1、一个线程模拟生产者，每100ms生产10个元素的字符串加入到ArrayList中，并通知消费者线程消费；
     * 2、两个线程模拟消费者，从ArrayList每次消费一个元素的字符串，将字符串转换为大写以后，重新放入ArrayList队首；
     * 3、消费者线程不能重复处理同一个元素，如果程序没有终止，且ArrayList没有新加入的值，则消费者线程处于等待ArrayList加入新值状态；
     * 4、ArrayList是非线程安全的集合，操作ArrayList应考虑加锁；
     * 5、如果ArrayList中的字符串个数超过100个，程序终止；
     **/


}
