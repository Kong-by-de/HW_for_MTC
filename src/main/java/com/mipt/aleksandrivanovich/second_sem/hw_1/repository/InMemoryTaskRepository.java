package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
@Primary
public class InMemoryTaskRepository implements TaskRepository {

  private final Map<String, Task> taskStorage = new ConcurrentHashMap<>();

  @Override
  public Task save(Task task) {
    taskStorage.put(task.getId(), task);
    return task;
  }

  @Override
  public Optional<Task> findById(String id) {
    return Optional.ofNullable(taskStorage.get(id));
  }

  @Override
  public List<Task> findAll() {
    return new ArrayList<>(taskStorage.values());
  }

  @Override
  public boolean deleteById(String id) {
    return taskStorage.remove(id) != null;
  }

  @Override
  public boolean existsById(String id) {
    return taskStorage.containsKey(id);
  }
}