package com.astocoding.test;

import com.astocoding.bean.BeanLifeTest;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
public class SpringBeanLifeTestRunner {


    @Resource
    private BeanLifeTest beanLifeTest;


    @Test
    public void test(){
        System.out.println("beanLifeTest = " + beanLifeTest);
    }
}
