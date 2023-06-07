package com.astocoding.bloomfilter.bloomFilter;


import jakarta.annotation.Resource;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class RedisBloomFilter {

    @Resource
    private RedisTemplate redisTemplate;


    /**
     * 删除Key
     *
     * @param key key
     */
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }

    /**
     * 向布隆过滤器中添加一个元素
     *
     * @param redisBloomFilterHelper 布隆过滤器帮助类
     * @param key                    key
     * @param value                  value
     * @param <T>                    泛型
     */
    public <T> void add(RedisBloomFilterHelper<T> redisBloomFilterHelper, String key, T value) {
        int[] offset = redisBloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            redisTemplate.opsForValue().setBit(key, i, true);
        }
    }

    /**
     * 向布隆过滤器中添加多个元素
     *
     * @param redisBloomFilterHelper 布隆过滤器帮助类
     * @param key                    key
     * @param values                 values
     * @param <T>                    泛型
     */
    public <T> void addList(RedisBloomFilterHelper<T> redisBloomFilterHelper, String key, T... values) {
        redisTemplate.executePipelined(new RedisCallback<Long>() {

            @Override
            public Long doInRedis(RedisConnection connection) throws DataAccessException {
                connection.openPipeline();
                for (T value : values) {
                    int[] offset = redisBloomFilterHelper.murmurHashOffset(value);
                    for (int i : offset) {
                        connection.setBit(key.getBytes(), i, true);
                    }
                }
                return null;
            }

        });
    }

    /**
     * 判断元素是否存在
     *
     * @param bloomFilterHelper 布隆过滤器帮助类
     * @param key               key
     * @param value             value
     * @param <T>               泛型
     * @return true 存在 false 不存在
     */
    public <T> boolean contains(RedisBloomFilterHelper<T> bloomFilterHelper, String key, T value) {
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
            if (!redisTemplate.opsForValue().getBit(key, i)) {
                return false;
            }
        }
        return true;
    }


}
