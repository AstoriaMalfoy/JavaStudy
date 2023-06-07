package com.astocoding;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/2/28 10:24
 */
public class WithoutCatch {
    public static void main(String[] args) {
        try {
            test();
        }finally {
            System.out.println("finally");
        }
    }

    // static method throws exception
    public static void test() throws NullPointerException {
        throw new NullPointerException("test");
    }



}
