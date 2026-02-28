package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.StubTaskRepository;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

  @Bean(name = "stubTaskRepository")
  public TaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}