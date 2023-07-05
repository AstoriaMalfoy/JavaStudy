package com.astocoding;

import com.astocoding.config.Config;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;


/**
 * Created by IntelliJ LITAO.
 *
 * @author litao
 * @since 2023/7/4 14:10
 */
@Slf4j
@Import({com.astocoding.config.Config.class})
@SpringBootApplication
public class Main implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        log.info("-------spring start success-------");
        Config.TestBean testBean = applicationContext.getBean(Config.TestBean.class);
        log.info("testBean:{}", testBean);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Main.applicationContext = applicationContext;
    }
}