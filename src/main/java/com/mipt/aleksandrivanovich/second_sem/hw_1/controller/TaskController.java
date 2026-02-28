package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

  private final TaskService taskService;

  public TaskController(TaskService taskService) {
    this.taskService = taskService;
  }

  @GetMapping
  public ResponseEntity<List<Task>> getAllTasks() {
    return ResponseEntity.ok(taskService.getAllTasks());
  }

  @GetMapping("/{id}")
  public ResponseEntity<Task> getTaskById(@PathVariable String id) {
    return taskService.getTaskById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Task> createTask(@RequestBody Task task) {
    Task created = taskService.createTask(
        task.getTitle(),
        task.getDescription()
    );
    return ResponseEntity.ok(created);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Task> updateTask(
      @PathVariable String id,
      @RequestBody Task task) {
    Task updated = taskService.updateTask(id, task);
    if (updated != null) {
      return ResponseEntity.ok(updated);
    }
    return ResponseEntity.notFound().build();
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTask(@PathVariable String id) {
    boolean deleted = taskService.deleteTask(id);
    if (deleted) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.notFound().build();
  }
}