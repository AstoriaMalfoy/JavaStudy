package org.example.test;

import jakarta.annotation.Resource;
import org.example.bean.BeanLifeTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanListTestRunner {

    @Resource
    private BeanLifeTest beanLifeTest;

    @Bean(initMethod = "initMethod")
    public BeanLifeTest beanLifeTest(){
        return new BeanLifeTest();
    }

    @Test
    public void test(){
        System.out.println("beanLifeTest = " + beanLifeTest);
    }
}
