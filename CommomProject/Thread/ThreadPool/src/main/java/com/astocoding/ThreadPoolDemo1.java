package com.astocoding;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/4/3 10:54
 */
public class ThreadPoolDemo1 {

    public static void main(String[] args) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                1,
                1,
                1,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(1),
                new MyThreadFactory("product"),
                new MyRejectedPolicy()
        );


        while (true){
            TimeUnit.SECONDS.sleep(1000);
            new Thread(()->{
                ArrayList<Future<Integer>> futures = new ArrayList<>();
                int productCount = 10;
                for (int i = 0; i < productCount; i++) {
                    try{
                        int finalI = i;
                        Future<Integer> submit = threadPoolExecutor.submit(() -> {
                            System.out.println("开始执行任务：" + finalI);
                            TimeUnit.MICROSECONDS.sleep(500);
                            System.out.println("任务执行完成：" + finalI);
                            return finalI * 10;
                        });
                        futures.add(submit);
                    }catch (Exception e){
                        System.out.println("线程池拒绝执行任务");
                    }
                }

                for (Future<Integer> future : futures) {
                    try {
                        System.out.println(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }

    }

    static class MyThreadFactory implements ThreadFactory {

        private static final AtomicInteger atomicInteger = new AtomicInteger(1);
        private final ThreadGroup threadGroup;
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final String threadNamePrefix;


        MyThreadFactory(String threadStartName){
            SecurityManager securityManager = System.getSecurityManager();
            threadGroup = (securityManager != null) ? securityManager.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
            threadNamePrefix = threadStartName;
        }

        public String getThreadFactoryName(){
            return threadNamePrefix;
        }



        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(threadGroup,r,namePrefix + atomicInteger.getAndIncrement(),0);
            if (t.isDaemon()){
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY){
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }


    public static class MyRejectedPolicy implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println("线程池拒绝执行任务");
            if (executor.getThreadFactory() instanceof MyThreadFactory){
                System.out.println("线程池名称：" + ((MyThreadFactory) executor.getThreadFactory()).getThreadFactoryName());
                MyThreadFactory myThreadFactory = (MyThreadFactory) executor.getThreadFactory();
                if ("product".equals(myThreadFactory.getThreadFactoryName())){
                    System.out.println("线程池名称：" + myThreadFactory.getThreadFactoryName());
                }
            }
        }
    }

}

