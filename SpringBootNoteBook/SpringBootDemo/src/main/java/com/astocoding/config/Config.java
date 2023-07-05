package com.astocoding.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/4 14:39
 */

public class Config {

    @Bean
    public TestBean getTestBean(){
        TestBean result = new TestBean();
        result.setName("stuart");
        result.setAge(12);
        return result;
    }

    @Data
    public static class TestBean{
        private String name;
        private Integer age;
    }
}
