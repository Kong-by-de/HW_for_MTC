package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для управления избранными задачами.
 */
@Service
@SessionScope
public class FavoritesService {

    private final List<String> favoriteTaskIds;

    public FavoritesService() {
        this.favoriteTaskIds = new ArrayList<>();
    }

    /**
     * Добавляет задачу в избранное.
     */
    public void addToFavorites(String taskId) {
        if (!favoriteTaskIds.contains(taskId)) {
            favoriteTaskIds.add(taskId);
        }
    }

    /**
     * Удаляет задачу из избранного.
     */
    public void removeFromFavorites(String taskId) {
        favoriteTaskIds.remove(taskId);
    }

    /**
     * Возвращает список всех избранных ID.
     */
    public List<String> getFavoriteTaskIds() {
        return new ArrayList<>(favoriteTaskIds);
    }

    /**
     * Проверяет, находится ли задача в избранном.
     */
    public boolean isFavorite(String taskId) {
        return favoriteTaskIds.contains(taskId);
    }
}