package com.astocoding;

import com.alibaba.fastjson.JSON;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.nio.charset.Charset;
import java.util.*;


/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/3/30 14:50
 */
public class CuckooHashFactory {

    @AllArgsConstructor
    public enum BUCKET_FORMAT {
        TWO_BUCKET(CuckooHashSetWithTwoBucket.class),
        ONE_BUCKET(CuckooHashSetWithOneBucket.class),
        ;

        private Class<? extends CuckooHashTemplate> cuckooHashTemplate;
    }


    public static CuckooHashTemplate createCuckooHash(BUCKET_FORMAT bucketFormat, int size, int deepLimit) {
        try {
            return bucketFormat.cuckooHashTemplate.getConstructor(int.class,int.class).newInstance(size,deepLimit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public interface CuckooHashTemplate<T> {

        void add(T value);

        void resize();

        void remove(T value);

        boolean contains(T value);

        int[] hash(T value);

        Status getStatus();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Status<T>{
        private int bucketSize;
        private List<T[]> hashList;
    }

    public static class CuckooHashSetWithTwoBucket<T> implements CuckooHashTemplate<T> {

        private  Integer deepLimit = 5;

        private Integer tableSize = 16;

        private T[] tableA = null;
        private T[] tableB = null;

        public CuckooHashSetWithTwoBucket(int size,int deepLimit) {
            int realSize;
            this.deepLimit = deepLimit;
            for (realSize = 1; realSize < size; realSize <<= 1) ;
            this.tableSize = realSize;
            this.tableA = (T[]) new Object[realSize];
            this.tableB = (T[]) new Object[realSize];
        }

        public void add(T value, Integer lastIndex, int deep) {
            if (deep > deepLimit) {
                resize();
                add(value, -1, 0);
                return;
            }

            int[] hash = hash(value);
            int hashA = hash[0];
            int hashB = hash[1];

            if (Objects.isNull(tableA[hashA])) {
                tableA[hashA] = value;
            } else if (Objects.isNull(tableB[hashB])) {
                tableB[hashB] = value;
            } else {
                if (hashA == lastIndex) {
                    T outValue = tableB[hashB];
                    tableB[hashB] = value;
                    add(outValue, hashB, deep + 1);
                } else {
                    T outValue = tableA[hashA];
                    tableA[hashA] = value;
                    add(outValue, hashA, deep + 1);
                }

            }

        }

        @Override
        public int[] hash(T value) {
            long hash64 = Hashing.murmur3_128().hashObject(value, (Funnel<T>) (from, into) -> into.putString(JSON.toJSONString(from), Charset.defaultCharset())).asLong();

            int tempHashA = (int) hash64;
            int tempHashB = (int) (hash64 >>> 32);

            int hashA = tempHashA + (1 * tempHashB);
            int hashB = tempHashA + (5 * tempHashB);
            if (hashA < 0) {
                hashA = ~hashA;
            }
            if (hashB < 0) {
                hashB = ~hashB;
            }
            hashA = hashA % tableSize;
            hashB = hashB % tableSize;
            return new int[]{hashA, hashB};

        }

        @Override
        public Status getStatus() {
           return new Status(tableSize, Arrays.asList(tableA,tableB));
        }


        @Override
        public void add(T value) {
            add(value, -1, 0);
        }

        @Override
        public void resize() {
            this.tableSize <<= 1;
            T[] tableATemp = this.tableA;
            T[] tableBTemp = this.tableB;
            this.tableA = (T[]) new Object[tableSize];
            this.tableB = (T[]) new Object[tableSize];
            for (int i = 0; i < tableATemp.length; i++) {
                if (Objects.nonNull(tableATemp[i])) {
                    this.add(tableATemp[i], -1, 0);
                }
            }
            for (int i = 0; i < tableBTemp.length; i++) {
                if (Objects.nonNull(tableBTemp[i])) {
                    this.add(tableBTemp[i], -1, 0);
                }
            }
        }

        @Override
        public void remove(T value) {
            int[] hash = hash(value);
            if (Objects.equals(tableA[hash[0]], value)) {
                tableA[hash[0]] = null;
            }
            if (Objects.equals(tableB[hash[1]], value)) {
                tableB[hash[1]] = null;
            }
        }

        @Override
        public boolean contains(T value) {
            int[] hash = hash(value);
            return Objects.equals(tableA[hash[0]], value) || Objects.equals(tableB[hash[1]], value);
        }


    }


    public static class CuckooHashSetWithOneBucket<T> implements CuckooHashTemplate<T>{
        private  Integer deepLimit = 5;

        private Integer tableSize = 16;

        private T[] tableA = null;

        public CuckooHashSetWithOneBucket(int size,int deepLimit) {
            int realSize;
            this.deepLimit = deepLimit;
            for (realSize = 1; realSize < size; realSize <<= 1) ;
            this.tableSize = realSize;
            this.tableA = (T[]) new Object[realSize];
        }

        public void add(T value, Integer lastIndex, int deep) {
            if (deep > deepLimit) {
                resize();
                add(value, -1, 0);
                return;
            }

            int[] hash = hash(value);
            int hashA = hash[0];
            int hashB = hash[1];

            if (Objects.isNull(tableA[hashA])) {
                tableA[hashA] = value;
            } else if (Objects.isNull(tableA[hashB])) {
                tableA[hashB] = value;
            } else {
                if (hashA == lastIndex) {
                    T outValue = tableA[hashB];
                    tableA[hashB] = value;
                    add(outValue, hashB, deep + 1);
                } else {
                    T outValue = tableA[hashA];
                    tableA[hashA] = value;
                    add(outValue, hashA, deep + 1);
                }

            }

        }

        @Override
        public int[] hash(T value) {
            long hash64 = Hashing.murmur3_128().hashObject(value, (Funnel<T>) (from, into) -> into.putString(JSON.toJSONString(from), Charset.defaultCharset())).asLong();

            int tempHashA = (int) hash64;
            int tempHashB = (int) (hash64 >>> 32);

            int hashA = tempHashA + (1 * tempHashB);
            int hashB = tempHashA + (5 * tempHashB);
            if (hashA < 0) {
                hashA = ~hashA;
            }
            if (hashB < 0) {
                hashB = ~hashB;
            }
            hashA = hashA % tableSize;
            hashB = hashB % tableSize;
            return new int[]{hashA, hashB};

        }

        @Override
        public Status getStatus() {
            return new Status(tableSize, Arrays.asList(tableA));
        }


        @Override
        public void add(T value) {
            add(value, -1, 0);
        }

        @Override
        public void resize() {
            this.tableSize <<= 1;
            T[] tableATemp = this.tableA;
            this.tableA = (T[]) new Object[tableSize];
            for (int i = 0; i < tableATemp.length; i++) {
                if (Objects.nonNull(tableATemp[i])) {
                    this.add(tableATemp[i], -1, 0);
                }
            }
        }

        @Override
        public void remove(T value) {
            int[] hash = hash(value);
            if (Objects.equals(tableA[hash[0]], value)) {
                tableA[hash[0]] = null;
            }
            if (Objects.equals(tableA[hash[1]], value)) {
                tableA[hash[1]] = null;
            }
        }

        @Override
        public boolean contains(T value) {
            int[] hash = hash(value);
            return Objects.equals(tableA[hash[0]], value) || Objects.equals(tableA[hash[1]], value);
        }
    }

    public static void main(String[] args) {
        CuckooHashSetWithTwoBucket<String> cuckooHashSetWithTwoBucket = new CuckooHashSetWithTwoBucket<>(10,20);
        for (int i = 0; i < 1000; i++) {
            cuckooHashSetWithTwoBucket.add(String.valueOf(i));
            System.out.println("==== insert " + i + " ====");
            System.out.println("[" + cuckooHashSetWithTwoBucket.tableSize + "]tableA:" + JSON.toJSONString(cuckooHashSetWithTwoBucket.tableA));
            System.out.println("[" + cuckooHashSetWithTwoBucket.tableSize + "]tableB:" + JSON.toJSONString(cuckooHashSetWithTwoBucket.tableB));
            System.out.println("=====================================");
        }
    }


}
