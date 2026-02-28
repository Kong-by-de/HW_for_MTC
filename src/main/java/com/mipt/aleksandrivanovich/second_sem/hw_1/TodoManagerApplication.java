package com.mipt.aleksandrivanovich.second_sem.hw_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Главный класс приложения Todo Manager.
 * Запускает Spring Boot приложение и включает поддержку AOP.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
public class TodoManagerApplication {

  /**
   * Точка входа в приложение.
   * Запускает Spring ApplicationContext и инициализирует все бины.
   *
   * @param args аргументы командной строки
   */
  public static void main(String[] args) {
    SpringApplication.run(TodoManagerApplication.class, args);
  }
}