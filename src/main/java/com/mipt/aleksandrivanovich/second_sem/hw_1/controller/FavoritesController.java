package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.FavoritesService;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Контроллер для управления избранными задачами.
 */
@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final TaskService taskService;

    public FavoritesController(FavoritesService favoritesService, TaskService taskService) {
        this.favoritesService = favoritesService;
        this.taskService = taskService;
    }

    /**
     * POST добавить задачу в избранное.
     */
    @PostMapping("/{taskId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable String taskId) {
        favoritesService.addToFavorites(taskId);
        return ResponseEntity.ok().build();
    }

    /**
     * DELETE удалить задачу из избранного.
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable String taskId) {
        favoritesService.removeFromFavorites(taskId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET получить список избранных задач.
     */
    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getFavorites() {
        List<String> favoriteIds = favoritesService.getFavoriteTaskIds();

        List<TaskResponseDto> favorites = favoriteIds.stream()
            .map(taskService::getTaskById)
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }
}