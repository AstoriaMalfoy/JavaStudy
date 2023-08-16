package org.example;

import org.example.entity.Persion;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.PrintStream;

/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/8/4 17:17
 */
public class Main {
    public static void main(String[] args) {
        System.setProperty("spring.config","D:\\Develop\\prvProject\\JavaStudy\\CommomProject\\SpringSourceReader\\src\\main\\resources");
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("${spring.config}:applicationContext.xml");
        Persion persion = applicationContext.getBean("person", Persion.class);
        System.out.println(persion);
    }
}