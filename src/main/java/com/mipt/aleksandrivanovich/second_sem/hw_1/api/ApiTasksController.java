package com.mipt.aleksandrivanovich.second_sem.hw_1.api;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.ExternalTaskDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.exception.ExternalApiException;
import com.mipt.aleksandrivanovich.second_sem.hw_1.exception.TaskNotFoundException;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TasksGatewayService;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tasks")
public class ApiTasksController {

    private final TasksGatewayService tasksGatewayService;

    public ApiTasksController(TasksGatewayService tasksGatewayService) {
        this.tasksGatewayService = tasksGatewayService;
    }

    @PostMapping
    public ResponseEntity<ExternalTaskDto> createTask(@Valid @RequestBody CreateTaskRequest request) {
        try {
            ExternalTaskDto created = tasksGatewayService.createTask(request.title(), request.completed());
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RequestNotPermitted e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Rate limit")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
            throw e;
        }
    }

    @GetMapping
    public ResponseEntity<List<ExternalTaskDto>> getTasks(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(defaultValue = "100") int limit) {
        try {
            List<ExternalTaskDto> tasks = tasksGatewayService.getTasks(completed, limit);
            return ResponseEntity.ok(tasks);
        } catch (RequestNotPermitted e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Rate limit")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
            throw e;
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ExternalTaskDto> getTask(@PathVariable Long id) {
        try {
            ExternalTaskDto task = tasksGatewayService.getTask(id);
            return ResponseEntity.ok(task);
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RequestNotPermitted e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Rate limit")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            tasksGatewayService.deleteTask(id);
            return ResponseEntity.noContent().build();
        } catch (TaskNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RequestNotPermitted e) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("Rate limit")) {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            }
            throw e;
        }
    }

    public record CreateTaskRequest(
        @jakarta.validation.constraints.NotBlank(message = "Title is required")
        @jakarta.validation.constraints.Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
        String title,

        @jakarta.validation.constraints.NotNull(message = "Completed status is required")
        boolean completed
    ) {
    }
}