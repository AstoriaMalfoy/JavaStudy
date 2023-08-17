package com.ascoding;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/28 16:03
 */
public class LockSupportDemo {

    private static boolean flag = true;

    public static void main(String[] args) {
        Thread thread = new Thread(){
            @Override
            public void run() {
                while (flag){

                }

                System.out.println("before first park");
                LockSupport.park();
                System.out.println("after first park");
                LockSupport.park();
                System.out.println("after second park");
            }
        };
        thread.start();
        flag = false;
        // sleep 200
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before  unpark");
        LockSupport.unpark(thread);
    }
}
