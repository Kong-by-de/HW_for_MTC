package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Сервис для демонстрации работы с несколькими репозиториями.
 * Показывает использование @Qualifier для явного выбора бина при инжекции.
 */
@Service
public class TaskStatisticsService {

  /**
   * Основной репозиторий (инжектируется через @Primary).
   */
  private final TaskRepository primaryRepository;

  /**
   * Stub-репозиторий (инжектируется через @Qualifier).
   */
  private final TaskRepository stubRepository;

  /**
   * Конструктор с инжекцией двух разных репозиториев.
   *
   * @param primaryRepository основной репозиторий (с @Primary)
   * @param stubRepository stub-репозиторий (с @Qualifier("stubTaskRepository"))
   */
  public TaskStatisticsService(
      TaskRepository primaryRepository,
      @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
    this.primaryRepository = primaryRepository;
    this.stubRepository = stubRepository;
  }

  /**
   * Сравнивает количество задач в двух репозиториях.
   *
   * @return строка с результатами сравнения
   */
  public String compareRepositories() {
    long primaryCount = primaryRepository.findAll().size();
    long stubCount = stubRepository.findAll().size();

    return String.format(
        "Primary repository: %d tasks, Stub repository: %d tasks",
        primaryCount, stubCount
    );
  }

  /**
   * Возвращает все задачи из основного репозитория.
   *
   * @return список задач из primaryRepository
   */
  public List<Task> getTasksFromPrimary() {
    return primaryRepository.findAll();
  }

  /**
   * Возвращает все задачи из stub-репозитория.
   *
   * @return список задач из stubRepository
   */
  public List<Task> getTasksFromStub() {
    return stubRepository.findAll();
  }

  /**
   * Определяет, в каком репозитории находится задача по ID.
   *
   * @param id идентификатор задачи
   * @return "primary", "stub" или "not found"
   */
  public String findTaskSource(String id) {
    if (primaryRepository.existsById(id)) {
      return "primary";
    }
    if (stubRepository.existsById(id)) {
      return "stub";
    }
    return "not found";
  }
}