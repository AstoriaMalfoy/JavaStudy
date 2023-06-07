package com.astocoding.unsafe;

import sun.misc.Unsafe;

/**
 *
 * java 使用 unsafe 类直接操作内存
 * Unsafe内存分配
 * // 分配内存
 * allocateMemory(long bytes)
 * // 重新分配内存
 * reallocateMemory(long address, long bytes)
 * // 内存初始化
 * setMemory(Object o, long offset, long bytes, byte value)
 * // 内存复制
 * copyMemory(long srcAddress, long destAddress, long bytes)
 * // 释放内存
 * freeMemory(long address)
 */
public class MemoryOperation {

    private static Unsafe unsafe;

    private static final int memorySize = 128;
    private static final int reAllocMemorySize = memorySize * 2;

    static {
        MemoryOperation.unsafe = UnsafeBase.getUnsafeObject();
    }

    public static void main(String[] args) {

        demoOperatorMemory();
//        badCase();
    }


    /**
     * 如果运行badCase会直接将内存沾满，操作系统将强行终止java虚拟机程序运行
     */
    private static void badCase(){
        int allocMemoryCount = 0;
        while (true){
            unsafe.allocateMemory(memorySize * 100);
            allocMemoryCount ++;

        }
    }

    private static void demoOperatorMemory() {
        // 分配内存够空间
        long addressStart = unsafe.allocateMemory(memorySize);
        printMemory(addressStart, memorySize);

        //内存空间初始化
        unsafe.setMemory(addressStart, memorySize, (byte) 0);
        System.out.println("init memory ....");
        printMemory(addressStart, memorySize);

        // 重新分配内存地址
        addressStart = unsafe.reallocateMemory(addressStart, reAllocMemorySize);
        System.out.println("re allocate memory....");
        printMemory(addressStart, reAllocMemorySize);

        // 重新初始化
        unsafe.setMemory(addressStart,reAllocMemorySize,(byte) 0);
        System.out.println("re init memory ... ");
        printMemory(addressStart,reAllocMemorySize);


        // 内存赋值
        for (int i = 0; i < memorySize ; i++) {
            unsafe.setMemory(addressStart  + i , 1 , (byte) i);
        }
        printMemory(addressStart,reAllocMemorySize);

        // 内存复制
        unsafe.copyMemory(addressStart,addressStart + memorySize , memorySize);
        System.out.println("memory copy ... ");
        printMemory(addressStart,reAllocMemorySize);
    }


    private static void printMemory(long addressStart, int memorySize) {
        System.out.print("[");
        for (int i = 0; i < memorySize; i++) {
            byte aByte = unsafe.getByte(addressStart + i);
            System.out.printf("0X%X,", aByte);
        }
        System.out.println("]");
    }


}
