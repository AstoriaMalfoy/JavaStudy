package com.astocoding.bean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BeanLifeTest implements BeanPostProcessor ,BeanNameAware{

    // Bean的是实例化阶段
    /**
     * 构造阶段执行
     */
    @Autowired
    public BeanLifeTest(){
        log.info("BeanLifeTest 构造方法执行 ... ");
    }



    // Bean的初始化阶段
    /**
     * invokeAwareMethod
     */
    @Override
    public void setBeanName(String name) {
        log.info("BeanLiftTest 感知类执行");
    }

}
