package com.mipt.aleksandrivanovich.second_sem.hw_1.dto;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Ответ с информацией о задаче")
public class TaskResponseDto {

    @Schema(description = "Идентификатор задачи", example = "1")
    private Long id;

    @Schema(description = "Заголовок задачи", example = "Купить продукты")
    private String title;

    @Schema(description = "Описание задачи", example = "Купить хлеб, молоко и яйца")
    private String description;

    @Schema(description = "Статус выполнения", example = "false")
    private boolean completed;

    @Schema(description = "Дата и время создания", example = "2026-06-01T10:30:00")
    private LocalDateTime createdAt;

    @Schema(description = "Дата выполнения", example = "2026-06-07")
    private LocalDate dueDate;

    @Schema(description = "Приоритет задачи", example = "HIGH")
    private Priority priority;

    @Schema(description = "Теги задачи (через запятую)", example = "важно,срочно")
    private String tags;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public String getTags() { return tags; }
    public void setTags(String tags) { this.tags = tags; }
}