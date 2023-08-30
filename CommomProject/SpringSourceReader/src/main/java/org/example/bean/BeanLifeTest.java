package org.example.bean;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class BeanLifeTest implements BeanNameAware , InitializingBean , BeanPostProcessor {

    // Bean的是实例化阶段
    /**
     * 构造阶段执行
     */
    @Autowired
    public BeanLifeTest(){
        System.out.println("BeanLifeTest constructor ... ");
    }


    // Bean的初始化阶段
    /**
     * invokeAwareMethod
     */
    @Override
    public void setBeanName(String name) {
        System.out.println("BeanLifeTest setBeanName ... " + name);
    }

    /**
     * Bean初始化阶段
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("BeanLifeTest afterPropertiesSet ... ");
    }


    /**
     * 自定义初始化方法
     */
    public void initMethod(){
        System.out.println("BeanLifeTest initMethod ... ");
    }


    @PostConstruct
    public void postConstruct(){
        System.out.println("BeanLifeTest postConstruct ... ");
    }

    @PreDestroy
    public void preDestroy(){
        System.out.println("BeanLifeTest preDestroy ... ");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanLifeTest postProcessBeforeInitialization ... " + beanName);
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }


    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        System.out.println("BeanLifeTest postProcessAfterInitialization ... " + beanName);
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
