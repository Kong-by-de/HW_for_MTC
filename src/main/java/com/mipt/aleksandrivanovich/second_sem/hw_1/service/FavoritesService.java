package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.List;

@Service
@SessionScope
public class FavoritesService {

    private final List<String> favoriteTaskIds;

    public FavoritesService() {
        this.favoriteTaskIds = new ArrayList<>();
    }

    public void addToFavorites(String taskId) {
        if (!favoriteTaskIds.contains(taskId)) {
            favoriteTaskIds.add(taskId);
        }
    }

    public void removeFromFavorites(String taskId) {
        favoriteTaskIds.remove(taskId);
    }

    public List<String> getFavoriteTaskIds() {
        return new ArrayList<>(favoriteTaskIds);
    }

    public boolean isFavorite(String taskId) {
        return favoriteTaskIds.contains(taskId);
    }
}