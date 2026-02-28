package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

  Task save(Task task);

  Optional<Task> findById(String id);

  List<Task> findAll();

  boolean deleteById(String id);

  boolean existsById(String id);
}