package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для управления задачами.
 * Обрабатывает HTTP запросы к API /api/tasks
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  /**
   * Конструктор с инжекцией сервиса
   * @param taskService сервис для бизнес-логики
   */
  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  /**
   * GET /api/tasks - получить все задачи
   * @return список задач
   */
  @GetMapping
  public ResponseEntity<List<Task>> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  /**
   * GET /api/tasks/{id} - получить задачу по ID
   * @param id идентификатор задачи
   * @return задача или 404 если не найдена
   */
  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable String id) {
    return taskService.getTaskById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  /**
   * POST /api/tasks - создать новую задачу
   * @param task данные задачи (title, description)
   * @return созданная задача
   */
  @PostMapping
  public ResponseEntity<Task> createTask(@Valid @RequestBody Task task) {
    Task created = taskService.createTask(
        task.getTitle(),
        task.getDescription()
    );
    return ResponseEntity.ok(created);
  }

  /**
   * PUT /api/tasks/{id} - обновить задачу
   * @param id идентификатор задачи
   * @param task новые данные
   * @return обновленная задача или 404
   */
  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(
      @PathVariable String id,
      @Valid @RequestBody Task task) {
    Task updated = taskService.updateTask(id, task);
    if (updated != null) {
      return ResponseEntity.ok(updated);
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * DELETE /api/tasks/{id} - удалить задачу
   * @param id идентификатор задачи
   * @return 204 если удалено, 404 если не найдено
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable String id) {
    boolean deleted = taskService.deleteTask(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }

  /**
   * Обработчик ошибок валидации.
   * Возвращает 400 Bad Request с описанием ошибок полей.
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return errors;
  }
}