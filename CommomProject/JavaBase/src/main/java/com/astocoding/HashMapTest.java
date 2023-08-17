package com.astocoding;


import java.util.HashMap;
import java.util.Map;

public class HashMapTest {
    class TestKey{
        private String keyContent;

        @Override
        public boolean equals(Object o){
            return false;
        }

        @Override
        public int hashCode(){
            return 1;
        }

    }


    public static void main(String[] args) {
        Map<TestKey,String> testMap = new HashMap<>();
        testMap.put(new HashMapTest().new TestKey(),"test1");
        testMap.put(new HashMapTest().new TestKey(),"test2");
        testMap.put(new HashMapTest().new TestKey(),"test3");
        testMap.put(new HashMapTest().new TestKey(),"test4");
        testMap.put(new HashMapTest().new TestKey(),"test5");
        testMap.put(new HashMapTest().new TestKey(),"test1");
        testMap.put(new HashMapTest().new TestKey(),"test2");
        testMap.put(new HashMapTest().new TestKey(),"test3");
        testMap.put(new HashMapTest().new TestKey(),"test4");
        testMap.put(new HashMapTest().new TestKey(),"test5");
        testMap.put(new HashMapTest().new TestKey(),"test1");
        testMap.put(new HashMapTest().new TestKey(),"test2");
        testMap.put(new HashMapTest().new TestKey(),"test3");
        testMap.put(new HashMapTest().new TestKey(),"test4");
        testMap.put(new HashMapTest().new TestKey(),"test5");
        testMap.put(new HashMapTest().new TestKey(),"test1");
        testMap.put(new HashMapTest().new TestKey(),"test2");
        testMap.put(new HashMapTest().new TestKey(),"test3");
        testMap.put(new HashMapTest().new TestKey(),"test4");
        testMap.put(new HashMapTest().new TestKey(),"test5");

    }
}
