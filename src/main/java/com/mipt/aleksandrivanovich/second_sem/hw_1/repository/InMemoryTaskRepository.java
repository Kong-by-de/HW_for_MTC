package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Реализация репозитория с хранением данных в памяти.
 * Основной репозиторий приложения, помеченный как @Primary.
 * Использует ConcurrentHashMap для потокобезопасности.
 */
@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {

  /**
   * Хранилище задач в памяти: ключ — id задачи, значение — объект Task.
   */
  private final Map<String, Task> taskStorage = new ConcurrentHashMap<>();

  /**
   * Сохраняет задачу в хранилище.
   *
   * @param task задача для сохранения
   * @return сохраненная задача
   */
  @Override
  public Task save(Task task) {
    taskStorage.put(task.getId(), task);
    return task;
  }

  /**
   * Находит задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей, если найдена, или пустой Optional
   */
  @Override
  public Optional<Task> findById(String id) {
    return Optional.ofNullable(taskStorage.get(id));
  }

  /**
   * Возвращает все задачи из хранилища.
   *
   * @return копию списка всех задач
   */
  @Override
  public List<Task> findAll() {
    return new ArrayList<>(taskStorage.values());
  }

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор задачи для удаления
   * @return true если задача была удалена, false если не найдена
   */
  @Override
  public boolean deleteById(String id) {
    return taskStorage.remove(id) != null;
  }

  /**
   * Проверяет наличие задачи по идентификатору.
   *
   * @param id идентификатор задачи
   * @return true если задача существует в хранилище
   */
  @Override
  public boolean existsById(String id) {
    return taskStorage.containsKey(id);
  }
}