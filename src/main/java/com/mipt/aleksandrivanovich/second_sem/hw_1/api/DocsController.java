package com.mipt.aleksandrivanovich.second_sem.hw_1.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/docs")
public class DocsController {

    @GetMapping
    public ResponseEntity<Map<String, String>> getDocs() {
        return ResponseEntity.ok(Map.of(
            "title", "API Documentation",
            "version", "1.0.0",
            "description", "This endpoint is accessible only with READ_PRIVILEGE authority"
        ));
    }
}