package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskCreateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskUpdateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskService;
import com.mipt.aleksandrivanovich.second_sem.hw_1.validation.OnCreate;
import com.mipt.aleksandrivanovich.second_sem.hw_1.validation.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
@Validated
@Tag(name = "Tasks", description = "API для управления задачами")
public class TaskController {

    private final TaskService taskService;

    @Value("${app.version:2.0.0}")
    private String apiVersion;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    @Operation(summary = "Получить все задачи")
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();
        return ResponseEntity.ok()
            .headers(h -> {
                h.add("X-Total-Count", String.valueOf(tasks.size()));
                h.add("X-API-Version", apiVersion);
            })
            .body(tasks);
    }

    @GetMapping("/with-attachments")
    @Operation(summary = "Получить задачи с вложениями (Решение N+1)")
    public ResponseEntity<List<TaskResponseDto>> getTasksWithAttachments(
        @RequestParam(defaultValue = "false") boolean completed) {
        List<TaskResponseDto> tasks = taskService.getTasksWithAttachments(completed);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID")
    public ResponseEntity<TaskResponseDto> getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id)
            .map(t -> ResponseEntity.ok().header("X-API-Version", apiVersion).body(t))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать задачу")
    public ResponseEntity<TaskResponseDto> createTask(
        @Validated(OnCreate.class) @Valid @RequestBody TaskCreateDto dto) {
        TaskResponseDto created = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу")
    public ResponseEntity<TaskResponseDto> updateTask(
        @PathVariable Long id,
        @Validated(OnUpdate.class) @Valid @RequestBody TaskUpdateDto dto) {
        return taskService.updateTask(id, dto)
            .map(t -> ResponseEntity.ok().body(t))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (taskService.deleteTask(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/bulk-complete")
    @Operation(summary = "Пакетное завершение задач (Транзакция)")
    public ResponseEntity<?> bulkCompleteTasks(@RequestBody Map<String, List<Long>> payload) {
        List<Long> ids = payload.get("ids");
        if (ids == null) {
            return ResponseEntity.badRequest().body("Missing 'ids' field");
        }

        try {
            taskService.bulkCompleteTasks(ids);
            return ResponseEntity.ok().body("Tasks completed successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}