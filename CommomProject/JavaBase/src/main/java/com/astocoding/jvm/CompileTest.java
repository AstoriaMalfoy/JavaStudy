package com.astocoding.jvm;


public class CompileTest {

    public static void main(String[] args){
        int current = 0;
        while (current < 30){
            testCompileTest();
            current++;
        }
    }

    public static void testCompileTest(){
        long startTime = System.nanoTime();
        long count = 1;
        for (int i = 0; i < 100000; i++) {
            count++;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("timer:").append(System.nanoTime() - startTime).append("ns");
        System.out.println(stringBuilder.toString());
    }
}
