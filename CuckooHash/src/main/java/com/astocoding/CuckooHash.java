package com.astocoding;

import com.alibaba.fastjson.JSON;
import com.google.common.hash.Funnel;
import com.google.common.hash.Hashing;
import com.google.common.hash.PrimitiveSink;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/3/30 14:50
 */
public class CuckooHash {

    public static class CuckooHashSet<T> {


        private static final Integer TABLE_COUNT = 2;

        private Integer tableSize = 1000;

        private T[] tableA = null;
        private T[] tableB = null;


        public CuckooHashSet() {
            this.tableA = (T[]) new Object[tableSize];
            this.tableB = (T[]) new Object[tableSize];
        }


        public CuckooHashSet(int size) {
            this.tableSize = size;
            this.tableA = (T[]) new Object[tableSize];
            this.tableB = (T[]) new Object[tableSize];
        }

        public void add(T value, Integer lastIndex) {
            long hash64 = Hashing.murmur3_128().hashObject(value, (Funnel<T>) (from, into) -> into.putString(JSON.toJSONString(from), Charset.defaultCharset())).asLong();

            int hashA = Math.abs((int) hash64) % tableSize;
            int hashB = Math.abs((int) hash64 >> 32) % tableSize;

            if (Objects.isNull(tableA[hashA])) {
                tableA[hashA] = value;
            } else if (Objects.isNull(tableB[hashB])) {
                tableB[hashB] = value;
            } else {
                if (hashA == lastIndex) {
                    T outValue = tableB[hashB];
                    tableB[hashB] = value;
                    add(outValue, hashB);
                } else {
                    T outValue = tableA[hashA];
                    tableA[hashA] = value;
                    add(outValue, hashA);
                }

            }

        }

        public void add(T value) {
            this.add(value, -1);
        }

        public static void main(String[] args) {
            CuckooHashSet<String> cuckooHashSet = new CuckooHashSet<>(10);
            for (int i = 0; i < 100; i++) {
                cuckooHashSet.add(String.valueOf(i));
                System.out.println("==== insert " + i + " ====");
                System.out.println("tableA:" + JSON.toJSONString(cuckooHashSet.tableA));
                System.out.println("tableB:" + JSON.toJSONString(cuckooHashSet.tableB));
                System.out.println("=====================================");
            }
        }


    }


}
