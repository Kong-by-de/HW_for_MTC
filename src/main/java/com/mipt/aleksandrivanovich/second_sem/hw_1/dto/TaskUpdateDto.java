package com.mipt.aleksandrivanovich.second_sem.hw_1.dto;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.validation.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

/**
 * DTO для обновления задачи.
 */
@Schema(description = "Данные для обновления задачи")
public class TaskUpdateDto {

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters", groups = OnUpdate.class)
    @Schema(description = "Заголовок задачи", example = "Купить продукты")
    private String title;

    @Size(max = 500, message = "Description must not exceed 500 characters", groups = OnUpdate.class)
    @Schema(description = "Описание задачи", example = "Купить хлеб, молоко и яйца")
    private String description;

    @Schema(description = "Статус выполнения", example = "true")
    private Boolean completed;

    @Schema(description = "Дата выполнения", example = "2025-01-20")
    private LocalDate dueDate;

    @Schema(description = "Приоритет задачи", example = "MEDIUM")
    private Priority priority;

    @Size(max = 5, message = "Maximum 5 tags allowed", groups = OnUpdate.class)
    @Schema(description = "Теги задачи", example = "[\"важно\"]")
    private Set<String> tags;

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Priority getPriority() { return priority; }
    public void setPriority(Priority priority) { this.priority = priority; }

    public Set<String> getTags() { return tags; }
    public void setTags(Set<String> tags) { this.tags = tags; }
}