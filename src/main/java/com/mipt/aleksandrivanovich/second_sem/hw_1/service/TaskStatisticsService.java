package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskStatisticsService {

  private final TaskRepository primaryRepository;
  private final TaskRepository stubRepository;

  public TaskStatisticsService(
      TaskRepository primaryRepository,
      @Qualifier("stubTaskRepository") TaskRepository stubRepository) {
    this.primaryRepository = primaryRepository;
    this.stubRepository = stubRepository;
  }

  public String compareRepositories() {
    long primaryCount = primaryRepository.findAll().size();
    long stubCount = stubRepository.findAll().size();

    return String.format(
        "Primary repository: %d tasks, Stub repository: %d tasks",
        primaryCount, stubCount
    );
  }

  public List<Task> getTasksFromPrimary() {
    return primaryRepository.findAll();
  }

  public List<Task> getTasksFromStub() {
    return stubRepository.findAll();
  }

  public String findTaskSource(String id) {
    if (primaryRepository.existsById(id)) {
      return "primary";
    }
    if (stubRepository.existsById(id)) {
      return "stub";
    }
    return "not found";
  }
}