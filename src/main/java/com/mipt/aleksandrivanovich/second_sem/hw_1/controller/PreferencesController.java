package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для управления пользовательскими настройками.
 */
@RestController
@RequestMapping("/api/preferences")
public class PreferencesController {

    private static final String VIEW_PREFERENCE_COOKIE = "viewPreference";

    /**
     * GET получить настройку отображения.
     */
    @GetMapping("/view")
    public ResponseEntity<Map<String, String>> getViewPreference(
        @CookieValue(value = VIEW_PREFERENCE_COOKIE, defaultValue = "detailed") String viewMode) {

        Map<String, String> response = new HashMap<>();
        response.put("viewMode", viewMode);

        return ResponseEntity.ok(response);
    }

    /**
     * POST установить настройку отображения.
     */
    @PostMapping("/view")
    public ResponseEntity<Map<String, String>> setViewPreference(
        @RequestParam String mode,
        HttpServletResponse response) {

        if (!"compact".equals(mode) && !"detailed".equals(mode)) {
            return ResponseEntity.badRequest().build();
        }

        Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE, mode);
        cookie.setMaxAge(365 * 24 * 60 * 60); // 1 год
        cookie.setPath("/");

        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("viewMode", mode);
        responseBody.put("message", "Preference saved successfully");

        return ResponseEntity.ok(responseBody);
    }

    /**
     * DELETE удалить настройку.
     */
    @DeleteMapping("/view")
    public ResponseEntity<Void> deleteViewPreference(HttpServletResponse response) {
        // Удаляем куку
        Cookie cookie = new Cookie(VIEW_PREFERENCE_COOKIE, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return ResponseEntity.noContent().build();
    }
}