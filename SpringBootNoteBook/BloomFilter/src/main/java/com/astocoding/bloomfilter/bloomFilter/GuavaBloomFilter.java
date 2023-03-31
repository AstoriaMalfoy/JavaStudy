package com.astocoding.bloomfilter.bloomFilter;


import com.google.common.base.Charsets;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

public class GuavaBloomFilter {


    public static void main(String[] args) {
        testObjectBloomFilter();
    }

    public static void testStringBloomFilter(){
        int count = 1000;
//        定义布隆过滤器
        BloomFilter<String> filter = BloomFilter.create(Funnels.stringFunnel(Charsets.UTF_8), 1000, 0.000000000001);

        for (int i = 0; i < count; i++) {
            filter.put(String.valueOf(i));
        }
        int matchCount = 0;
        for (int i = 0; i < count + 1000; i++){
            if (filter.mightContain(String.valueOf(i))){
                matchCount++;
            }
        }
        System.out.println("total count :" + count  + " match count " + matchCount);
    }

    public static void testObjectBloomFilter(){
        int count = 1000;
        BloomFilter<User> filter = BloomFilter.create(
                (Funnel<User>) (from, into) -> into.putString(from.toString(), Charsets.UTF_8),
                1000,
                0.00000000000000000000000000000000000000000000000000001);
        for (int i = 0; i < count; i++) {
            filter.put(User.of(String.valueOf(i)));
        }
        int matchCount = 0;
        for (int i=0;i<count + 1000 ; i++){
            if (filter.mightContain(User.of(String.valueOf(i)))){
                matchCount++;
            }
        }
        System.out.println("total count :" + count  + " match count " + matchCount);
    }


    @Data
    @ToString
    @EqualsAndHashCode
    @AllArgsConstructor
    private static class User{
        private String userId;

        public static User of(String userId){
            return new User(userId);
        }
    }

}
