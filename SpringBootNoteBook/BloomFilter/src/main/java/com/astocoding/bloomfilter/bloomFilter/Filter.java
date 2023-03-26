package com.astocoding.bloomfilter.bloomFilter;

import orestes.bloomfilter.CountingBloomFilter;
import orestes.bloomfilter.FilterBuilder;

public class Filter {
    private static CountingBloomFilter<String> filter;

    static {
        filter = new FilterBuilder(1000, 0.01).countingBits(8).buildCountingBloomFilter();
    }

    public static void main(String[] args) {
        filter.add("test");
        System.out.println(filter.contains("test"));
        System.out.println(filter.contains("test2"));
        filter.clear();
    }

}
