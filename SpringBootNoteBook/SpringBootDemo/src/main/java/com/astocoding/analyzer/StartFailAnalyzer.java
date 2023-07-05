package com.astocoding.analyzer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.boot.diagnostics.FailureAnalysis;
import org.springframework.boot.diagnostics.FailureAnalyzer;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

@Slf4j
public class StartFailAnalyzer implements FailureAnalyzer , BeanFactoryAware, EnvironmentAware {

    private BeanFactory beanFactory;
    private Environment environment;
    @Override
    public FailureAnalysis analyze(Throwable failure) {
        log.error("start fail" + failure.getClass());
        log.error("reason :" + failure.getCause());
        log.error("message :",failure);
        log.error("current beanfactory:" + beanFactory);
        log.error("current environment:" + environment);
        return null;
//        return new FailureAnalysis("start fail", "please check your config", failure);
    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
