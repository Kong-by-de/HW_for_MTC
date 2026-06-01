package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskStatisticsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private final TaskStatisticsService statisticsService;

    public StatisticsController(TaskStatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    @GetMapping("/priority")
    public ResponseEntity<List<TaskStatisticsService.TaskPriorityCount>> getTasksCountByPriority() {
        return ResponseEntity.ok(statisticsService.getTasksCountByPriority());
    }
}