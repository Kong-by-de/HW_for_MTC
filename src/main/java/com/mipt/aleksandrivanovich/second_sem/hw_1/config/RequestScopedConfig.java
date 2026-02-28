package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

@Configuration
public class RequestScopedConfig {

  @Bean
  @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public RequestScopedBean requestScopedBean() {
    return new RequestScopedBean();
  }

  @Bean
  @Scope("prototype")
  public PrototypeScopedBean prototypeScopedBean() {
    return new PrototypeScopedBean();
  }
}