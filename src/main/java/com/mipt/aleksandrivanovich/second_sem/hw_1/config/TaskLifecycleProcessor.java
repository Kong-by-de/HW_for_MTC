package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(TaskLifecycleProcessor.class);

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    String beanClassName = bean.getClass().getSimpleName();

    if (beanClassName.contains("TaskService") || beanClassName.contains("TaskRepository")) {
      logger.info("🔧 [BEFORE INIT] Bean '{}' of type '{}' is being initialized",
          beanName, beanClassName);
    }

    return bean;
  }
  
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    String beanClassName = bean.getClass().getSimpleName();

    if (beanClassName.contains("TaskService") || beanClassName.contains("TaskRepository")) {
      logger.info("✅ [AFTER INIT] Bean '{}' of type '{}' has been initialized",
          beanName, beanClassName);
    }

    return bean;
  }
}