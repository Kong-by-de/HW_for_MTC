package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.config.TaskMapper;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskCreateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskStatisticsService.TaskPriorityCount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class TaskStatisticsServiceTest {

    @Autowired
    private TaskStatisticsService statisticsService;

    @Autowired
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        createTask("Task 1", Priority.HIGH);
        createTask("Task 2", Priority.HIGH);
        createTask("Task 3", Priority.MEDIUM);
        createTask("Task 4", Priority.LOW);
    }

    @Test
    void getTasksCountByPriority_ShouldReturnCorrectCounts() {
        List<TaskPriorityCount> stats = statisticsService.getTasksCountByPriority();

        assertThat(stats).hasSize(3);

        TaskPriorityCount high = stats.stream()
            .filter(s -> "HIGH".equals(s.getPriority()))
            .findFirst()
            .orElseThrow();

        TaskPriorityCount medium = stats.stream()
            .filter(s -> "MEDIUM".equals(s.getPriority()))
            .findFirst()
            .orElseThrow();

        TaskPriorityCount low = stats.stream()
            .filter(s -> "LOW".equals(s.getPriority()))
            .findFirst()
            .orElseThrow();

        assertThat(high.getCount()).isEqualTo(2);
        assertThat(medium.getCount()).isEqualTo(1);
        assertThat(low.getCount()).isEqualTo(1);
    }

    private TaskResponseDto createTask(String title, Priority priority) {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle(title);
        dto.setDescription("Description");
        dto.setPriority(priority);
        dto.setDueDate(LocalDate.now().plusDays(7));
        dto.setTags("test");
        return taskService.createTask(dto);
    }
}