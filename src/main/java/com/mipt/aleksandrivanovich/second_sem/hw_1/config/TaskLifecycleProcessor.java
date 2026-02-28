package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

/**
 * Процессор жизненного цикла бинов.
 * Логирует этапы создания и инициализации бинов TaskService и TaskRepository.
 */
@Component
public class TaskLifecycleProcessor implements BeanPostProcessor {

  private static final Logger logger = LoggerFactory.getLogger(TaskLifecycleProcessor.class);

  /**
   * Вызывается перед инициализацией бина (после создания экземпляра).
   * Логирует событие для бинов TaskService и TaskRepository.
   *
   * @param bean     создаваемый бин
   * @param beanName имя бина в контексте
   * @return тот же экземпляр бина (без изменений)
   * @throws BeansException если возникла ошибка обработки
   */
  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    String beanClassName = bean.getClass().getSimpleName();

    if (beanClassName.contains("TaskService") || beanClassName.contains("TaskRepository")) {
      logger.info("🔧 [BEFORE INIT] Bean '{}' of type '{}' is being initialized",
          beanName, beanClassName);
    }

    return bean;
  }

  /**
   * Вызывается после инициализации бина (после @PostConstruct).
   * Логирует событие для бинов TaskService и TaskRepository.
   *
   * @param bean     инициализированный бин
   * @param beanName имя бина в контексте
   * @return тот же экземпляр бина (без изменений)
   * @throws BeansException если возникла ошибка обработки
   */
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