package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Заглушка репозитория с фиксированными тестовыми данными.
 * Используется для тестирования и демонстрации работы с несколькими репозиториями.
 * Не сохраняет данные — методы save() и deleteById() только логируют вызовы.
 */
public class StubTaskRepository implements TaskRepository {

  /**
   * Список предопределённых тестовых задач.
   * Инициализируется тремя задачами при создании репозитория.
   */
  private final List<Task> stubTasks;

  /**
   * Конструктор, инициализирующий репозиторий тестовыми данными.
   * Создаёт три предопределённые задачи с id: stub-1, stub-2, stub-3.
   */
  public StubTaskRepository() {
    this.stubTasks = Arrays.asList(
        new Task("stub-1", "Первая тестовая задача", "Описание первой задачи", false),
        new Task("stub-2", "Вторая тестовая задача", "Описание второй задачи", true),
        new Task("stub-3", "Третья тестовая задача", "Описание третьей задачи", false)
    );
  }

  /**
   * Имитация сохранения задачи.
   * Не сохраняет данные в хранилище, только логирует вызов.
   *
   * @param task задача для сохранения
   * @return та же задача без изменений
   */
  @Override
  public Task save(Task task) {
    System.out.println("StubRepository: save called for task: " + task.getTitle());
    return task;
  }

  /**
   * Находит задачу по идентификатору в списке тестовых данных.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей, если найдена, или пустой Optional
   */
  @Override
  public Optional<Task> findById(String id) {
    return stubTasks.stream()
        .filter(task -> task.getId().equals(id))
        .findFirst();
  }

  /**
   * Возвращает все тестовые задачи из репозитория.
   *
   * @return копию списка всех тестовых задач
   */
  @Override
  public List<Task> findAll() {
    System.out.println("StubRepository: returning " + stubTasks.size() + " tasks");
    return new ArrayList<>(stubTasks);
  }

  /**
   * Имитация удаления задачи.
   * Не удаляет данные, только логирует вызов.
   *
   * @param id идентификатор задачи для удаления
   * @return всегда false (удаление не выполняется)
   */
  @Override
  public boolean deleteById(String id) {
    System.out.println("StubRepository: delete called for id: " + id);
    return false;
  }

  /**
   * Проверяет наличие задачи по идентификатору в списке тестовых данных.
   *
   * @param id идентификатор задачи
   * @return true если задача существует в списке stubTasks
   */
  @Override
  public boolean existsById(String id) {
    return stubTasks.stream().anyMatch(task -> task.getId().equals(id));
  }
}