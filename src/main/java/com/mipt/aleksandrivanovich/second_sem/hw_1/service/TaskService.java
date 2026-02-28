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

@Service
public class TaskService {

  private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

  private final TaskRepository taskRepository;

  private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
    logger.info("📦 TaskService создан через конструктор");
  }

  @PostConstruct
  public void init() {
    logger.info("🚀 @PostConstruct: Инициализация TaskService и загрузка кэша...");

    List<Task> tasks = taskRepository.findAll();
    for (Task task : tasks) {
      taskCache.put(task.getId(), task);
    }

    logger.info("✅ Кэш загружен: {} задач", taskCache.size());
  }

  @PreDestroy
  public void cleanup() {
    logger.info("🛑 @PreDestroy: Очистка ресурсов TaskService...");
    logger.info("📊 В кэше осталось задач: {}", taskCache.size());
    logger.info("💾 Сохранение статистики перед закрытием...");

    taskCache.clear();

    logger.info("✅ Ресурсы освобождены");
  }

  public Task createTask(String title, String description) {
    Task task = new Task();
    task.setId(UUID.randomUUID().toString());
    task.setTitle(title);
    task.setDescription(description);
    task.setCompleted(false);
    return taskRepository.save(task);
  }

  public Optional<Task> getTaskById(String id) {
    return taskRepository.findById(id);
  }

  public List<Task> getAllTasks() {
    return taskRepository.findAll();
  }

  public Task updateTask(String id, Task updatedTask) {
    if (!taskRepository.existsById(id)) {
      return null;
    }
    updatedTask.setId(id);
    return taskRepository.save(updatedTask);
  }

  public boolean deleteTask(String id) {
    return taskRepository.deleteById(id);
  }

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