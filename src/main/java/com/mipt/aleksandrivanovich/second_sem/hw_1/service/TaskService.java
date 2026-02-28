package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskService {

  private final TaskRepository taskRepository;

  public TaskService(TaskRepository taskRepository) {
    this.taskRepository = taskRepository;
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