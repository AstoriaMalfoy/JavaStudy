package com.astocoding;

import java.util.Map;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/5/6 15:42
 */
public class GetEnv {
    public static void main(String[] args){
        Map<String, String> allEnv = System.getenv();
        System.out.println(allEnv);
    }
}
