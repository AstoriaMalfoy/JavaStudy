package com.astocoding.bloomfilter;

import com.astocoding.bloomfilter.bloomFilter.RedisBloomFilter;
import com.astocoding.bloomfilter.bloomFilter.RedisBloomFilterHelper;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.PrimitiveSink;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@Slf4j
@SpringBootTest
class BloomGuavaBloomFilterApplicationTests {


    @Resource
    RedisTemplate redisTemplate;
    @Resource
    RedisBloomFilter redisBloomFilter;


    private final static String KEY = "BLOOM_FILTER_REDIS_KEY";

    @Test
    void setAndPutValue() {
//		redisTemplate.opsForValue().set("name", "astocoding");
        System.out.println(redisTemplate.opsForValue().get("name"));
    }

    @Test
    void testRedisBloomFilter() {
        RedisBloomFilterHelper<String> redisBloomFilterHelper = new RedisBloomFilterHelper<>((Funnel<String>) (from, into) -> into.putString(from, Charsets.UTF_8), 1000, 0.01);


        for (int i = 0; i < 1000; i++) {
            redisBloomFilter.add(redisBloomFilterHelper, KEY, String.valueOf(i));
        }

        int containCount = 0;
        for (int i = 0; i < 10000 + 1000; i++) {
            if (redisBloomFilter.contains(redisBloomFilterHelper, KEY, String.valueOf(i))) {
                containCount++;
            }
        }

        log.info("containCount: {}", containCount);
    }


}
