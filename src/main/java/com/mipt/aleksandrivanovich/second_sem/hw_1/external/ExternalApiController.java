package com.mipt.aleksandrivanovich.second_sem.hw_1.external;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.ExternalTaskDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/external/v1")
public class ExternalApiController {

    private final Map<Long, ExternalTaskDto> tasks = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Глобальное состояние нестабильности
    private volatile String unstableMode = null;

    @PostMapping("/tasks")
    public ResponseEntity<ExternalTaskDto> createTask(@RequestBody ExternalTaskDto taskDto) {
        if (unstableMode != null) {
            return handleUnstableResponse();
        }

        Long id = idGenerator.getAndIncrement();
        ExternalTaskDto created = new ExternalTaskDto(id, taskDto.title(), taskDto.completed());
        tasks.put(id, created);

        URI location = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(id)
            .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<ExternalTaskDto>> getTasks(
        @RequestParam(required = false) Boolean completed,
        @RequestParam(defaultValue = "100") int limit) {
        if (unstableMode != null) {
            return handleUnstableResponse();
        }

        List<ExternalTaskDto> result = tasks.values().stream()
            .filter(task -> completed == null || task.completed() == completed)
            .limit(limit)
            .toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<ExternalTaskDto> getTask(@PathVariable Long id) {
        if (unstableMode != null) {
            return handleUnstableResponse();
        }

        ExternalTaskDto task = tasks.get(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if (unstableMode != null) {
            return handleUnstableResponse();
        }

        tasks.remove(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unstable")
    public ResponseEntity<String> unstable(@RequestParam String mode) throws InterruptedException {
        if ("off".equalsIgnoreCase(mode)) {
            unstableMode = null;
            return ResponseEntity.ok("Unstable mode DISABLED");
        }

        unstableMode = mode;

        return switch (mode) {
            case "timeout" -> {
                Thread.sleep(10000);
                yield ResponseEntity.ok("Unstable mode ENABLED: timeout");
            }
            case "500" -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unstable mode ENABLED: 500");
            case "429" -> ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                .header("Retry-After", "60")
                .body("Unstable mode ENABLED: 429");
            case "html" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.TEXT_HTML)
                .body("<html><body>Unstable mode ENABLED: html</body></html>");
            default -> {
                unstableMode = null;
                yield ResponseEntity.badRequest().body("Unknown mode: " + mode);
            }
        };
    }

    private <T> ResponseEntity<T> handleUnstableResponse() {
        if (unstableMode == null) {
            return ResponseEntity.ok(null);
        }

        return switch (unstableMode) {
            case "500" -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            case "429" -> ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).build();
            case "timeout" -> {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                yield ResponseEntity.status(HttpStatus.GATEWAY_TIMEOUT).build();
            }
            case "html" -> ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                .contentType(MediaType.TEXT_HTML)
                .build();
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        };
    }
}