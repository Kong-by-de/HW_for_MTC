package com.mipt.aleksandrivanovich.second_sem.hw_1.model;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * Модель задачи (To-Do item).
 * Представляет данные задачи в системе управления задачами.
 * Содержит информацию об идентификаторе, заголовке, описании и статусе выполнения.
 */
public class Task {

  /**
   * Уникальный идентификатор задачи.
   * Генерируется автоматически при создании задачи.
   */
  private String id;

  /**
   * Заголовок задачи.
   * Обязательное поле, не может быть пустым.
   */
  @NotBlank(message = "Title cannot be empty")
  private String title;

  /**
   * Описание задачи.
   * Содержит подробную информацию о задаче.
   */
  private String description;

  /**
   * Статус выполнения задачи.
   * true - задача выполнена, false - задача не выполнена.
   */
  private boolean completed;

  /**
   * Пустой конструктор.
   * Требуется для фреймворков (Spring, Jackson и др.).
   */
  public Task() {
  }

  /**
   * Конструктор с параметрами.
   * Создает новую задачу с заданными параметрами.
   *
   * @param id          уникальный идентификатор задачи
   * @param title       заголовок задачи
   * @param description описание задачи
   * @param completed   статус выполнения задачи
   */
  public Task(String id, String title, String description, boolean completed) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.completed = completed;
  }

  /**
   * Возвращает идентификатор задачи.
   *
   * @return уникальный идентификатор задачи
   */
  public String getId() {
    return id;
  }

  /**
   * Возвращает заголовок задачи.
   *
   * @return заголовок задачи
   */
  public String getTitle() {
    return title;
  }

  /**
   * Возвращает описание задачи.
   *
   * @return описание задачи
   */
  public String getDescription() {
    return description;
  }

  /**
   * Проверяет статус выполнения задачи.
   *
   * @return true если задача выполнена, false иначе
   */
  public boolean isCompleted() {
    return completed;
  }

  /**
   * Устанавливает идентификатор задачи.
   *
   * @param id уникальный идентификатор задачи
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Устанавливает заголовок задачи.
   *
   * @param title заголовок задачи
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Устанавливает описание задачи.
   *
   * @param description описание задачи
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Устанавливает статус выполнения задачи.
   *
   * @param completed true если задача выполнена, false иначе
   */
  public void setCompleted(boolean completed) {
    this.completed = completed;
  }

  /**
   * Сравнивает этот объект с указанным объектом на равенство.
   * Две задачи считаются равными, если у них совпадают id и статус completed.
   *
   * @param o объект для сравнения
   * @return true если объекты равны, false иначе
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Task task = (Task) o;
    return completed == task.completed && Objects.equals(id, task.id);
  }

  /**
   * Возвращает хэш-код задачи.
   * Хэш-код вычисляется на основе полей id и completed.
   *
   * @return хэш-код задачи
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, completed);
  }

  /**
   * Возвращает строковое представление задачи.
   * Используется для отладки и логирования.
   *
   * @return строковое представление задачи в формате Task{id, title, description, completed}
   */
  @Override
  public String toString() {
    return "Task{" +
        "id='" + id + '\'' +
        ", title='" + title + '\'' +
        ", description='" + description + '\'' +
        ", completed=" + completed +
        '}';
  }
}