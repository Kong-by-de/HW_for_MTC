package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class StubTaskRepository implements TaskRepository {

  private final List<Task> stubTasks;

  public StubTaskRepository() {
    this.stubTasks = Arrays.asList(
        new Task("stub-1", "Первая тестовая задача", "Описание первой задачи", false),
        new Task("stub-2", "Вторая тестовая задача", "Описание второй задачи", true),
        new Task("stub-3", "Третья тестовая задача", "Описание третьей задачи", false)
    );
  }

  @Override
  public Task save(Task task) {
    System.out.println("StubRepository: save called for task: " + task.getTitle());
    return task;
  }

  @Override
  public Optional<Task> findById(String id) {
    return stubTasks.stream()
        .filter(task -> task.getId().equals(id))
        .findFirst();
  }

  @Override
  public List<Task> findAll() {
    System.out.println("StubRepository: returning " + stubTasks.size() + " tasks");
    return new ArrayList<>(stubTasks);
  }

  @Override
  public boolean deleteById(String id) {
    System.out.println("StubRepository: delete called for id: " + id);
    return false;
  }

  @Override
  public boolean existsById(String id) {
    return stubTasks.stream().anyMatch(task -> task.getId().equals(id));
  }
}