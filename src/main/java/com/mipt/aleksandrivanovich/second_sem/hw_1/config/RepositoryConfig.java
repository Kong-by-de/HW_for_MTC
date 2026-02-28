package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.StubTaskRepository;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Конфигурационный класс для регистрации бинов репозиториев.
 * Определяет создание StubTaskRepository через @Bean.
 */
@Configuration
public class RepositoryConfig {

  /**
   * Создает бин StubTaskRepository с именем "stubTaskRepository".
   * Используется для тестирования и инжектируется через @Qualifier.
   *
   * @return экземпляр StubTaskRepository с тестовыми данными
   */
  @Bean(name = "stubTaskRepository")
  public TaskRepository stubTaskRepository() {
    return new StubTaskRepository();
  }
}