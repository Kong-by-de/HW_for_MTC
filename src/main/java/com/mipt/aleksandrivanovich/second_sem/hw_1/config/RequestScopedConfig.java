package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;

/**
 * Конфигурационный класс для регистрации бинов с разными областями видимости.
 * Определяет request-scoped и prototype-scoped бины.
 */
@Configuration
public class RequestScopedConfig {

  /**
   * Создает бин RequestScopedBean с областью видимости request.
   * Новый экземпляр создается для каждого HTTP-запроса.
   *
   * @return новый экземпляр RequestScopedBean
   */
  @Bean
  @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
  public RequestScopedBean requestScopedBean() {
    return new RequestScopedBean();
  }

  /**
   * Создает бин PrototypeScopedBean с областью видимости prototype.
   * Новый экземпляр создается при каждом обращении к ApplicationContext.
   *
   * @return новый экземпляр PrototypeScopedBean
   */
  @Bean
  @Scope("prototype")
  public PrototypeScopedBean prototypeScopedBean() {
    return new PrototypeScopedBean();
  }
}