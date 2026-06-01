package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.FavoritesService;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesController {

    private final FavoritesService favoritesService;
    private final TaskService taskService;

    public FavoritesController(FavoritesService favoritesService, TaskService taskService) {
        this.favoritesService = favoritesService;
        this.taskService = taskService;
    }

    @PostMapping("/{taskId}")
    public ResponseEntity<Void> addToFavorites(@PathVariable Long taskId) {
        favoritesService.addToFavorites(String.valueOf(taskId));
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> removeFromFavorites(@PathVariable Long taskId) {
        favoritesService.removeFromFavorites(String.valueOf(taskId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getFavorites() {
        List<String> favoriteIds = favoritesService.getFavoriteTaskIds();

        List<TaskResponseDto> favorites = favoriteIds.stream()
            .map(id -> {
                try {
                    return taskService.getTaskById(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    return java.util.Optional.<TaskResponseDto>empty();
                }
            })
            .filter(java.util.Optional::isPresent)
            .map(java.util.Optional::get)
            .collect(Collectors.toList());

        return ResponseEntity.ok(favorites);
    }
}