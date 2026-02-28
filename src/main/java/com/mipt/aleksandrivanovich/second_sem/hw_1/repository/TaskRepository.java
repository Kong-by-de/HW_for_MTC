package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс репозитория для операций CRUD с задачами.
 * Определяет методы для работы с хранилищем задач.
 * Реализуется классами InMemoryTaskRepository и StubTaskRepository.
 */
public interface TaskRepository {

  /**
   * Сохраняет задачу в хранилище.
   *
   * @param task задача для сохранения
   * @return сохраненная задача
   */
  Task save(Task task);

  /**
   * Находит задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей, если найдена, или пустой Optional
   */
  Optional<Task> findById(String id);

  /**
   * Возвращает все задачи из хранилища.
   *
   * @return список всех задач
   */
  List<Task> findAll();

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор задачи для удаления
   * @return true если задача была удалена, false если не найдена
   */
  boolean deleteById(String id);

  /**
   * Проверяет наличие задачи по идентификатору.
   *
   * @param id идентификатор задачи
   * @return true если задача существует в хранилище
   */
  boolean existsById(String id);
}