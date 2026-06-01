package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    private Task savedTask;

    @BeforeEach
    void setUp() {
        Task task = new Task();
        task.setTitle("Test Task");
        task.setDescription("Test Description");
        task.setCompleted(false);
        task.setPriority(Priority.HIGH);
        task.setDueDate(LocalDate.now().plusDays(7));
        task.setTags("test,urgent");

        savedTask = taskRepository.save(task);
    }

    @Test
    void findByCompleted_ShouldReturnTasks() {
        List<Task> tasks = taskRepository.findByCompleted(false);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getTitle()).isEqualTo("Test Task");
    }

    @Test
    void findByPriority_ShouldReturnTasks() {
        List<Task> tasks = taskRepository.findByPriority(Priority.HIGH);

        assertThat(tasks).hasSize(1);
        assertThat(tasks.get(0).getPriority()).isEqualTo(Priority.HIGH);
    }

    @Test
    void findById_ShouldReturnTask() {
        Optional<Task> found = taskRepository.findById(savedTask.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Test Task");
    }

    @Test
    void findById_NotFound_ShouldReturnEmpty() {
        Optional<Task> found = taskRepository.findById(999L);

        assertThat(found).isEmpty();
    }

    @Test
    void findTasksDueInDateRange_ShouldReturnTasks() {
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(14);

        List<Task> tasks = taskRepository.findTasksDueInDateRange(startDate, endDate);

        assertThat(tasks).hasSize(1);
    }

    @Test
    void searchByTitle_ShouldReturnTasks() {
        List<Task> tasks = taskRepository.searchByTitle("Test");

        assertThat(tasks).hasSize(1);
    }

    @Test
    void deleteById_ShouldDeleteTask() {
        taskRepository.deleteById(savedTask.getId());

        Optional<Task> found = taskRepository.findById(savedTask.getId());
        assertThat(found).isEmpty();
    }
}