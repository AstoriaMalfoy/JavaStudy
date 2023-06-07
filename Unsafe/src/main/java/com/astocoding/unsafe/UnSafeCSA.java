package com.astocoding.unsafe;

import sun.misc.Unsafe;

import javax.swing.*;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/17 10:05
 *
 * Unsafe的cas比较的时候，只能比较并且交换三个类型的值，分别是Object，int , long (基本类型)
 *
 */
public class UnSafeCSA {
    private static Unsafe unsafe = UnsafeBase.getUnsafeObject();

    public static long valueOffset;

    public static long boxedValueOffset;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(UnSafeCSA.class.getDeclaredField("value"));
            boxedValueOffset = unsafe.objectFieldOffset(UnSafeCSA.class.getDeclaredField("boxedValue"));
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            System.out.println("the UnsafeCas not activity");
        }
    }

    private int value;

    private Integer boxedValue;

    private ExecutorService executors = Executors.newFixedThreadPool(20);

    private CountDownLatch workStart = new CountDownLatch(1);

    private CountDownLatch mainThreadStart = new CountDownLatch(20);


    public UnSafeCSA(int init,Integer boxedValue){
        this.value = init;
        this.boxedValue = boxedValue;
    }




    public boolean setValue(int require,int expect){
        return unsafe.compareAndSwapInt(this, valueOffset,expect,require);
    }

    public boolean setObjectValue(Object require,Integer expect){
        return unsafe.compareAndSwapObject(this,boxedValueOffset,expect,require);
    }

    public int getValue(){
        return this.value;
    }

    public Integer getBoxedValue(){
        return this.boxedValue;
    }


    public void doTest() throws InterruptedException {
        for (int i=0;i<20;i++){
            executors.execute(()->{
                try {
                    workStart.await();
                    boolean doWork = false;
                    while(!doWork){
                        int tempValue = getValue();
                        if (tempValue <= 0){
                            System.out.println("thread " + Thread.currentThread().getName() + " do not get a value ....");
                            mainThreadStart.countDown();
                            return;
                        }
                        doWork = setValue(tempValue-1,tempValue);
                        System.out.println("thread " + Thread.currentThread().getName() + " update result " + doWork );
                    }
                    mainThreadStart.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            });
        }
        workStart.countDown();
        System.out.println(workStart.getCount());
        mainThreadStart.await();
    }

    public void doBoxedValueCAS(){
        Integer except = getBoxedValue();
        Integer tempBoxedValue = 20;
        boolean b1 = setObjectValue( tempBoxedValue, except);
        System.out.println("update result is " + b1);
        System.out.println("current boxed value is " + boxedValue);
        except = 100;
        tempBoxedValue = 200;
        System.out.println("current boxed value is " + boxedValue);

    }




    public static void main(String[] args) throws InterruptedException {
        UnSafeCSA unSafeCSA = new UnSafeCSA(5,10);
        unSafeCSA.doBoxedValueCAS();
        System.out.println(unSafeCSA.getValue());
    }


}
