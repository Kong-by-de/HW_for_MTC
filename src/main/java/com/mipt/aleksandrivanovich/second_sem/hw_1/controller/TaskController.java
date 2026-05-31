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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST контроллер для управления задачами.
 */
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
    @Operation(summary = "Получить все задачи", description = "Возвращает список всех задач")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Успешно получены задачи",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponseDto.class)))
    })
    public ResponseEntity<List<TaskResponseDto>> getAllTasks() {
        List<TaskResponseDto> tasks = taskService.getAllTasks();

        return ResponseEntity.ok()
            .headers(httpHeaders -> {
                httpHeaders.add("X-Total-Count", String.valueOf(tasks.size()));
                httpHeaders.add("X-API-Version", apiVersion);
            })
            .body(tasks);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача найдена",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Задача не найдена",
            content = @Content)
    })
    public ResponseEntity<TaskResponseDto> getTaskById(
        @Parameter(description = "Идентификатор задачи", required = true)
        @PathVariable String id) {

        return taskService.getTaskById(id)
            .map(task -> ResponseEntity.ok()
                .header("X-API-Version", apiVersion)
                .body(task))
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Создать новую задачу", description = "Создаёт новую задачу с указанными параметрами")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Задача успешно создана",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Некорректные данные",
            content = @Content)
    })
    public ResponseEntity<TaskResponseDto> createTask(
        @Parameter(description = "Данные для создания задачи", required = true)
        @Validated(OnCreate.class) @Valid @RequestBody TaskCreateDto dto) {

        TaskResponseDto created = taskService.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("X-API-Version", apiVersion)
            .body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить задачу", description = "Обновляет существующую задачу")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Задача успешно обновлена",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = TaskResponseDto.class))),
        @ApiResponse(responseCode = "404", description = "Задача не найдена",
            content = @Content),
        @ApiResponse(responseCode = "400", description = "Некорректные данные",
            content = @Content)
    })
    public ResponseEntity<TaskResponseDto> updateTask(
        @Parameter(description = "Идентификатор задачи", required = true)
        @PathVariable String id,
        @Parameter(description = "Данные для обновления задачи", required = true)
        @Validated(OnUpdate.class) @Valid @RequestBody TaskUpdateDto dto) {

        return taskService.updateTask(id, dto)
            .map(task -> ResponseEntity.ok()
                .header("X-API-Version", apiVersion)
                .body(task))
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по идентификатору")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Задача успешно удалена"),
        @ApiResponse(responseCode = "404", description = "Задача не найдена",
            content = @Content)
    })
    public ResponseEntity<Void> deleteTask(
        @Parameter(description = "Идентификатор задачи", required = true)
        @PathVariable String id) {

        boolean deleted = taskService.deleteTask(id);
        if (deleted) {
            return ResponseEntity.noContent()
                .header("X-API-Version", apiVersion)
                .build();
        }
        return ResponseEntity.notFound().build();
    }
}