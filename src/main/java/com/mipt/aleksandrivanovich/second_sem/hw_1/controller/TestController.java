package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskStatisticsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

  private final TaskStatisticsService statisticsService;

  public TestController(TaskStatisticsService statisticsService) {
    this.statisticsService = statisticsService;
  }

  @GetMapping("/repositories")
  public Map<String, Object> compareRepositories() {
    Map<String, Object> result = new HashMap<>();
    result.put("comparison", statisticsService.compareRepositories());
    result.put("primaryTasks", statisticsService.getTasksFromPrimary());
    result.put("stubTasks", statisticsService.getTasksFromStub());
    return result;
  }
}