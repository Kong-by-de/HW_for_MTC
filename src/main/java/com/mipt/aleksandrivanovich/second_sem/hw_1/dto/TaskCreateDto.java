package com.mipt.aleksandrivanovich.second_sem.hw_1.dto;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.validation.OnCreate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO для создания новой задачи.
 */
@Schema(description = "Данные для создания новой задачи")
public class TaskCreateDto {

    @NotBlank(message = "Title is required", groups = OnCreate.class)
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters", groups = OnCreate.class)
    @Schema(description = "Заголовок задачи", example = "Купить продукты", requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters", groups = OnCreate.class)
    @Schema(description = "Описание задачи", example = "Купить хлеб, молоко и яйца")
    private String description;

    @FutureOrPresent(message = "Due date cannot be in the past", groups = OnCreate.class)
    @Schema(description = "Дата выполнения задачи", example = "2025-01-20")
    private LocalDate dueDate;

    @NotNull(message = "Priority is required", groups = OnCreate.class)
    @Schema(description = "Приоритет задачи", example = "HIGH", requiredMode = Schema.RequiredMode.REQUIRED)
    private Priority priority;

    @Size(max = 5, message = "Maximum 5 tags allowed", groups = OnCreate.class)
    @Schema(description = "Теги задачи", example = "[\"важно\", \"срочно\"]")
    private Set<String> tags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
}