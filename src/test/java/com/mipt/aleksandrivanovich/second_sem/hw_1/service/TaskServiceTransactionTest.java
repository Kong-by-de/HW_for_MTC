package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.config.TaskMapper;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskCreateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskServiceTransactionTest {

    @Autowired
    private TaskService taskService;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskMapper taskMapper;

    private Task savedTask1;
    private Task savedTask2;

    @BeforeEach
    void setUp() {
        TaskCreateDto dto1 = createValidDto("Task 1");
        TaskCreateDto dto2 = createValidDto("Task 2");

        TaskResponseDto response1 = taskService.createTask(dto1);
        TaskResponseDto response2 = taskService.createTask(dto2);

        savedTask1 = taskRepository.findById(response1.getId()).orElseThrow();
        savedTask2 = taskRepository.findById(response2.getId()).orElseThrow();
    }

    @Test
    void bulkCompleteTasks_ShouldCompleteAllTasks() {
        List<Long> ids = List.of(savedTask1.getId(), savedTask2.getId());

        taskService.bulkCompleteTasks(ids);

        Task updated1 = taskRepository.findById(savedTask1.getId()).orElseThrow();
        Task updated2 = taskRepository.findById(savedTask2.getId()).orElseThrow();

        assertThat(updated1.isCompleted()).isTrue();
        assertThat(updated2.isCompleted()).isTrue();
    }

    @Test
    void bulkCompleteTasks_WithInvalidId_ShouldRollback() {
        List<Long> ids = List.of(savedTask1.getId(), 999L); // 999L не существует

        assertThatThrownBy(() -> taskService.bulkCompleteTasks(ids))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Task not found");

        Task notUpdated1 = taskRepository.findById(savedTask1.getId()).orElseThrow();
        Task notUpdated2 = taskRepository.findById(savedTask2.getId()).orElseThrow();

        assertThat(notUpdated1.isCompleted()).isFalse();
        assertThat(notUpdated2.isCompleted()).isFalse();
    }

    private TaskCreateDto createValidDto(String title) {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle(title);
        dto.setDescription("Description");
        dto.setPriority(Priority.HIGH);
        dto.setDueDate(LocalDate.now().plusDays(7));
        dto.setTags("test");
        return dto;
    }
}