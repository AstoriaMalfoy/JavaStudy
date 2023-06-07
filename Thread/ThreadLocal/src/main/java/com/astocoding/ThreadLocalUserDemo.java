package com.astocoding;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/22 14:24
 */
public class ThreadLocalUserDemo {


    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(200, new ThreadFactory() {
            int threadId = 0;
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r,"inner thread " + (threadId++));
            }
        });
        InnerClass innerClass = new InnerClass();
        for (int i = 0 ; i < 100 ; i ++){
            executorService.execute(()->{
                String currentThreadName = Thread.currentThread().getName();
                String setInnerValue = "the " + currentThreadName + " innerValue";
                innerClass.setValue(setInnerValue);
                System.out.println("[" + Thread.currentThread().getName() + "] set value .. " + setInnerValue);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("[" + Thread.currentThread().getName() + "] get value .. " + innerClass.getValue());
            });
        }
    }


    public static class InnerClass{
        ThreadLocal<String> strLocalThread = new ThreadLocal<>();
        public void setValue(String str){
            strLocalThread.set(str);
        }
        public String getValue(){
            return strLocalThread.get();
        }
    }

}
