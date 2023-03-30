package com.astocoding.bloomfilter;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@Slf4j
@SpringBootTest
class BloomFilterApplicationTests {

	@Resource
	RedisTemplate<String, String> redisTemplate;

	@Test
	public void testRedis() {
		redisTemplate.opsForValue().set("test", "asasasasasa");
		System.out.println(redisTemplate.opsForValue().get("test"));
	}


	@Test
	public void redissonBloomFilter() {
		Config config = new Config();
		config.setCodec(new StringCodec());

		config.useSingleServer().setAddress("redis://127.0.0.1:6379");

		RedissonClient redissonClient = Redisson.create(config);

		RBloomFilter<String> bloomFilter = redissonClient.getBloomFilter("bloomFilter");
		bloomFilter.tryInit(1000, 0.00003);

		for (int i = 0; i < 1000; i++) {
			bloomFilter.add(String.valueOf(i));
		}

		int hitCount = 0;
		for (int i = 0; i < 1000  + 1000; i++) {
			if (bloomFilter.contains(String.valueOf(i))) {
				hitCount++;
			}
		}
		log.info("hitCount: {}", hitCount);
	}

}
