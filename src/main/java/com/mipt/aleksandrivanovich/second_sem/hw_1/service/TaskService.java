package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Сервис для управления задачами.
 * Содержит бизнес-логику приложения и кэширование задач.
 * Демонстрирует использование @PostConstruct и @PreDestroy.
 */
@Service
public class TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  /**
   * Репозиторий для работы с данными задач.
   */
  private final TaskRepository taskRepository;

  /**
   * Кэш задач в памяти для быстрого доступа.
   */
  private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

  /**
   * Конструктор с инжекцией репозитория.
   *
   * @param taskRepository репозиторий для работы с данными
   */
  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
    logger.info("📦 TaskService создан через конструктор");
  }

  /**
   * Метод инициализации, вызывается после создания бина.
   * Загружает задачи из репозитория в кэш при старте приложения.
   */
  @PostConstruct
  public void init() {
    logger.info("🚀 @PostConstruct: Инициализация TaskService и загрузка кэша...");

    List<Task> tasks = taskRepository.findAll();
    for (Task task : tasks) {
      taskCache.put(task.getId(), task);
    }

    logger.info("✅ Кэш загружен: {} задач", taskCache.size());
  }

  /**
   * Метод очистки, вызывается перед уничтожением бина.
   * Логирует статистику и освобождает ресурсы кэша.
   */
  @PreDestroy
  public void cleanup() {
    logger.info("🛑 @PreDestroy: Очистка ресурсов TaskService...");
    logger.info("📊 В кэше осталось задач: {}", taskCache.size());
    logger.info("💾 Сохранение статистики перед закрытием...");

    taskCache.clear();

    logger.info("✅ Ресурсы освобождены");
  }

  /**
   * Создает новую задачу с автоматически сгенерированным ID.
   *
   * @param title заголовок задачи
   * @param description описание задачи
   * @return созданная задача
   */
  public Task createTask(String title, String description) {
    Task task = new Task();
    task.setId(UUID.randomUUID().toString());
    task.setTitle(title);
    task.setDescription(description);
    task.setCompleted(false);
    return taskRepository.save(task);
  }

  /**
   * Получает задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return Optional с задачей, если найдена
   */
  public Optional<Task> getTaskById(String id) {
    return taskRepository.findById(id);
  }

  /**
   * Возвращает все задачи из репозитория.
   *
   * @return список всех задач
   */
  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  /**
   * Обновляет существующую задачу.
   *
   * @param id идентификатор задачи
   * @param updatedTask данные для обновления
   * @return обновленная задача или null если не найдена
   */
  public Task updateTask(String id, Task updatedTask) {
    if (!taskRepository.existsById(id)) {
      return null;
    }
    updatedTask.setId(id);
    return taskRepository.save(updatedTask);
  }

  /**
   * Удаляет задачу по идентификатору.
   *
   * @param id идентификатор задачи
   * @return true если задача удалена
   */
  public boolean deleteTask(String id) {
    return taskRepository.deleteById(id);
  }

  /**
   * Помечает задачу как выполненную.
   *
   * @param id идентификатор задачи
   * @return true если задача обновлена, false если не найдена
   */
  public boolean completeTask(String id) {
    return taskRepository.findById(id)
        .map(task -> {
          task.setCompleted(true);
          taskRepository.save(task);
          return true;
        })
        .orElse(false);
  }
}