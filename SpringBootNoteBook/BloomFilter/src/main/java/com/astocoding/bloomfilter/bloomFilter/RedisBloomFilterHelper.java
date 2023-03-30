package com.astocoding.bloomfilter.bloomFilter;

import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;

public class RedisBloomFilterHelper<T> {

    private int numHashFunctions;

    private int bitSize;

    private Funnel<T> funnel;

    public RedisBloomFilterHelper(int expectInsertCount) {
        this.funnel = (Funnel<T>) Funnels.stringFunnel(Charset.defaultCharset());
        this.bitSize = optimalNumOfBit(expectInsertCount, 0.01);
        this.numHashFunctions = optimalNumOfHashFunctions(expectInsertCount, bitSize);
    }

    public RedisBloomFilterHelper(Funnel<T> funnel, int expectInsertCount, double fpp) {
        this.funnel = funnel;
        this.bitSize = optimalNumOfBit(expectInsertCount, fpp);
        this.numHashFunctions = optimalNumOfHashFunctions(expectInsertCount, bitSize);
    }

    /**
     * 计算需要被设置为1的哈希位置
     *
     * @param value 值
     * @return 哈希位置
     */
    public int[] murmurHashOffset(T value) {
        int[] offset = new int[numHashFunctions];
        long hash64 = Hashing.murmur3_128().hashObject(value, funnel).asLong();
        int hash1 = (int) hash64;
        int hash2 = (int) (hash64 >>> 32);
        for (int i = 1; i <= numHashFunctions; i++) {
            int nextHash = hash1 + i * hash2;
            if (nextHash < 0) {
                nextHash = ~nextHash;
            }
            offset[i - 1] = nextHash % bitSize;
        }
        return offset;
    }


    /**
     * 计算bit数组长度
     *
     * @param n 期望插入的元素个数
     * @param p 期望的误判率
     * @return bit数组长度
     */
    private int optimalNumOfBit(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (int) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }


    /**
     * 计算hash函数个数
     *
     * @param n 期望插入的元素个数
     * @param m bit数组长度
     * @return hash函数个数
     */
    private int optimalNumOfHashFunctions(long n, long m) {
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

}
