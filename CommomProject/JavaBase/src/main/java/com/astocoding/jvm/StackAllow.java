package com.astocoding.jvm;

import lombok.SneakyThrows;
import org.junit.Test;

public class StackAllow {
    @Test
    @SneakyThrows
    public void startStackAllow(){
        for (int i = 0 ; i < 100000000 ; i++){
            User user = new User();
        }
        System.out.println("startStackAllow");
        Thread.sleep(1000000);
    }

    private static class User{}
}
