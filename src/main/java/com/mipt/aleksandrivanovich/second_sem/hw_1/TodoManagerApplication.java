package com.mipt.aleksandrivanovich.second_sem.hw_1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TodoManagerApplication {

  public static void main(String[] args) {
    SpringApplication.run(TodoManagerApplication.class, args);
  }
}