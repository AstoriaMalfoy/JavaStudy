package com.astocoding.config;

import com.astocoding.bean.BeanLifeTest;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.CanIgnoreReturnValue;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class BeanInstanceProcessor implements InstantiationAwareBeanPostProcessor {

    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
//        if (Objects.equals(beanClass, BeanLifeTest.class)){
            log.info("Bean实例化之前的扩展点... beanClass: {} , beanName: {}", beanClass, beanName);
//        }
        return InstantiationAwareBeanPostProcessor.super.postProcessBeforeInstantiation(beanClass, beanName);
    }


    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
//        if (bean instanceof BeanLifeTest){
            log.info("Bean实例化之后的扩展点... beanClass: {} , beanName: {}", bean, beanName);
//        }
        return InstantiationAwareBeanPostProcessor.super.postProcessAfterInstantiation(bean, beanName);
    }

}
