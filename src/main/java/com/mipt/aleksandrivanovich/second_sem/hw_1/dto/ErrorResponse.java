package com.mipt.aleksandrivanovich.second_sem.hw_1.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.Map;

/**
 * DTO для единообразного представления ошибок.
 */
@Schema(description = "Ответ с информацией об ошибке")
public class ErrorResponse {

    @Schema(description = "Временная метка ошибки", example = "2025-01-15T10:30:00Z")
    private Instant timestamp;

    @Schema(description = "HTTP статус код", example = "400")
    private int status;

    @Schema(description = "Краткое описание ошибки", example = "Bad Request")
    private String error;

    @Schema(description = "Детальное сообщение об ошибке", example = "Validation failed")
    private String message;

    @Schema(description = "Путь запроса", example = "/api/tasks")
    private String path;

    @Schema(description = "Дополнительные детали ошибки")
    private Map<String, Object> details;

    public ErrorResponse() {
        this.timestamp = Instant.now();
    }

    public ErrorResponse(int status, String error, String message, String path) {
        this();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, Object> details) {
        this(status, error, message, path);
        this.details = details;
    }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getError() { return error; }
    public void setError(String error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }

    public Map<String, Object> getDetails() { return details; }
    public void setDetails(Map<String, Object> details) { this.details = details; }
}