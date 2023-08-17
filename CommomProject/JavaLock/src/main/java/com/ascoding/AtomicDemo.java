package com.ascoding;

import lombok.Data;
import lombok.NoArgsConstructor;
import sun.misc.Unsafe;
import sun.nio.ch.DirectBuffer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.*;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/16 16:38
 * <p>
 * <p>
 * Atomic 相关类 ：
 * * 基本类型
 * AtomicBoolean --> 底层依旧是 AtomicInteger
 * AtomicInteger
 * AtomicLong
 * * 代理类型
 * AtomicReference
 */
public class AtomicDemo {


    Executor executor = Executors.newFixedThreadPool(10);

    AtomicBoolean atomicBoolean;

    AtomicInteger atomicInteger;

    AtomicLong atomicLong;

    AtomicReference atomicReference;

    AtomicStampedReference atomicStampedReference;


    public static void main(String[] args) throws InterruptedException {
        new AtomicDemo().compAtomicReferenceAndAtomStampReference();
    }

    /**
     * 在对原子类进行set的时候 ,  需要使用CAS的方式进行SET，如果只是调用普通的set方法，还是会有线程安全问题，因为set方法只是直接对值进行更新的
     *
     * @throws InterruptedException
     */
    public void testAtomIntegerSec() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger(10);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        for (int i = 0; i < 10; i++) {
            executor.execute(() -> {
                int result = atomicInteger.get();
                result -= 1;
                atomicInteger.set(result);
                System.out.println("the update result is " + result);
//                boolean b = atomicInteger.compareAndSet(result, result - 1);
//                System.out.println("thread " + Thread.currentThread().getName() + " update result " + b + " get result is " + result);
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        System.out.println(atomicInteger.get());
    }


    public void testAtomicReference() throws InterruptedException {
        AtomicReference<Person> atomicReference = new AtomicReference<>(new Person("test", 12));

        CountDownLatch countDownLatch = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.execute(() -> {
                boolean workSuccess = false;
                while (!workSuccess) {
                    Person person = atomicReference.get();
                    Person newPerson = new Person("test" + finalI, 20 + finalI);
                    workSuccess = atomicReference.compareAndSet(person, newPerson);
                    if (workSuccess) {
                        System.out.println("the thread " + Thread.currentThread().getName() + " modify Object from [" + person.toString() + "] to [" + newPerson.toString() + "]");
                    }
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
    }


    /**
     * AtomicStampReference 是为了解决 AtomicReference 中存在的ABA问题而开发出来的，而解决ABA的问题就是添加一个时间戳 ， 底层是将引用对象和时间戳在一起封装成一个pair,
     * 但是AtomicStampReference没有直接获取Pair的方法，只有单独获取引用和时间戳的方法，这就导致了在一些特殊的情况，这两次获取到的对象就已经不具备一致性了，
     * 所以建议可以自己通过AtomicStampReference的方式，将自己封装的对象中添加时间戳或者版本字段来解决ABA问题
     */
    public void testAtomicStampedReference() {
        AtomicStampedReference<Person> atomicStampedReference = new AtomicStampedReference<Person>(Person.of("test", 123), (int) System.currentTimeMillis());
        CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            int finalI = i;
            executor.execute(() -> {
                Boolean workSuccess = false;
                while (!workSuccess) {
                    Person exceptReference = atomicStampedReference.getReference();
                    int exceptStamp = atomicStampedReference.getStamp();


                    Person updateReference = Person.of("test" + finalI, 23 + finalI);
                    int updateStamp = (int) System.currentTimeMillis();

                    workSuccess = atomicStampedReference.compareAndSet(
                            exceptReference,
                            updateReference,
                            exceptStamp,
                            updateStamp
                    );
                    if (workSuccess) {
                        System.out.println("the thread " + Thread.currentThread().getName() + " update atomicStampReference from [" + exceptReference.toString() + "," + exceptStamp + "] to [" + updateReference.toString() + "," + updateStamp + "]");
                    }
                }
                countDownLatch.countDown();
            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void compAtomicReferenceAndAtomStampReference() throws InterruptedException {
        Integer cCount = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(50);

        CountDownLatch atomRef = new CountDownLatch(100);
        CountDownLatch atomStampRef = new CountDownLatch(100);

        AtomicReference<Pair<Person>> atomicReference = new AtomicReference<>(Pair.of(Person.of("test", 1), System.currentTimeMillis()));
        AtomicStampedReference<Person> atomicStampedReference = new AtomicStampedReference<>(Person.of("test", 2), (int) System.currentTimeMillis());


        long atomicReferenceStart = System.currentTimeMillis();
        AtomicInteger totalCasCount = new AtomicInteger();
        // atomicReference
        for (int i = 0; i < cCount; i++) {
            int finalI = i;
            executor.execute(() -> {
                boolean workSuccess = false;
                int casCount = 0;
                while (!workSuccess) {
                    Pair<Person> exceptReference = atomicReference.get();
                    Pair<Person> updateReference = Pair.of(Person.of("test" + finalI, 1 + finalI), System.currentTimeMillis());
                    workSuccess = atomicReference.compareAndSet(exceptReference, updateReference);
                    casCount++;
                    if (workSuccess) {
//                        System.out.println("the thread " + Thread.currentThread().getName() + " update reference from [" + exceptReference.toString() + "] to [" + updateReference.toString() + "] , casCount = " + casCount);
                        totalCasCount.addAndGet(casCount);
                        atomRef.countDown();
                    }
                }
            });
        }
        atomRef.await();
        long atomicReferenceEnd = System.currentTimeMillis();
        AtomicInteger totalCasCount2 = new AtomicInteger();
        // atomicStampPreference
        for (int i = 0; i < cCount; i++) {
            int finalI = i;
            executor.execute(() -> {
                boolean workSuccess = false;
                int casCount = 0;
                while (!workSuccess) {
                    Person exceptReference = atomicStampedReference.getReference();
                    int exceptStamp = atomicStampedReference.getStamp();

                    Person updateReference = Person.of("test" + finalI, 1 + finalI);
                    int updateStamp = (int) System.currentTimeMillis();
                    workSuccess = atomicStampedReference.compareAndSet(exceptReference, updateReference, exceptStamp, updateStamp);
                    casCount++;
                    if (workSuccess) {
//                        System.out.println("the thread " + Thread.currentThread().getName() + " update reference from [" + exceptReference.toString() + "," + exceptStamp + "] to [" + updateReference.toString() + "," + updateStamp + "] , caseCount = " + casCount);
                        totalCasCount2.addAndGet(casCount);
                        atomStampRef.countDown();
                    }
                }
            });
        }
        atomStampRef.await();
        long atomicStampReferenceEnd = System.currentTimeMillis();
        System.out.println("[AtomicReference] time cost " + (atomicReferenceEnd - atomicReferenceStart) + " casCount = " + totalCasCount.get());
        System.out.println("[AtomicStampReference] time cost " + (atomicStampReferenceEnd - atomicReferenceEnd) + " casCount = " + totalCasCount2.get());

    }


    @Data
    @NoArgsConstructor
    public static class Person {


        public static Person of(String name, Integer age) {
            return new Person(name, age);
        }

        private Person(String name, Integer age) {
            this.name = name;
            this.age = age;
        }

        public String toString() {
            return this.name + "," + this.age;
        }

        private String name;
        private Integer age;
    }

    @Data
    @NoArgsConstructor
    public static class Pair<T> {
        private T data;
        private long timeStamp;

        private Pair(T data, long timeStamp) {
            this.data = data;
            this.timeStamp = timeStamp;
        }

        public static <T> Pair<T> of(T data, long timeStamp) {
            return new Pair<>(data, timeStamp);
        }

    }


}
