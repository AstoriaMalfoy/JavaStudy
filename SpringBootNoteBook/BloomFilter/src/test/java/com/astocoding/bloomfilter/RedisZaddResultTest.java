package com.astocoding.bloomfilter;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/8 12:14
 */
@Slf4j
@SpringBootTest
public class RedisZaddResultTest {

    @Resource
    RedisTemplate redisTemplate;

    @Test
    public void zaddTest() {
        String key = "scm_testKey";
        redisTemplate.opsForZSet().remove(key, "testVlaue");
        Boolean testVlaue = redisTemplate.opsForZSet().addIfAbsent(key, "testVlaue", 12);
        System.out.println(testVlaue);
        redisTemplate.opsForZSet().remove(key, "testVlaue");
        testVlaue = redisTemplate.opsForZSet().addIfAbsent(key, "testVlaue", 13);
        System.out.println(testVlaue);
    }
}
