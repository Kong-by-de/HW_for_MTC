package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskCreateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskUpdateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Priority;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TaskControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    private String baseUrl;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/tasks";
        taskRepository.findAll().forEach(task -> taskRepository.deleteById(task.getId()));
    }

    @Test
    void getAllTasks_EmptyList_ReturnsOk() {
        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void getAllTasks_WithTasks_ReturnsAllTasks() {
        TaskCreateDto dto1 = createValidDto("Task 1");
        TaskCreateDto dto2 = createValidDto("Task 2");
        taskService.createTask(dto1);
        taskService.createTask(dto2);

        ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getTaskById_ExistingId_ReturnsTask() {
        TaskCreateDto dto = createValidDto("Test Task");
        TaskResponseDto created = taskService.createTask(dto);

        ResponseEntity<TaskResponseDto> response = restTemplate.getForEntity(
            baseUrl + "/" + created.getId(), TaskResponseDto.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", Objects.requireNonNull(response.getBody()).getTitle());
    }

    @Test
    void getTaskById_NonExistingId_ReturnsNotFound() {
        ResponseEntity<TaskResponseDto> response = restTemplate.getForEntity(
            baseUrl + "/999", TaskResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createTask_ValidTask_ReturnsCreatedTask() {
        TaskCreateDto dto = createValidDto("New Task");

        ResponseEntity<TaskResponseDto> response = restTemplate.postForEntity(
            baseUrl, dto, TaskResponseDto.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        assertEquals("New Task", response.getBody().getTitle());
        assertEquals(Priority.HIGH, response.getBody().getPriority());
    }

    @Test
    void createTask_EmptyTitle_ReturnsBadRequest() {
        TaskCreateDto invalidDto = new TaskCreateDto();
        invalidDto.setTitle("");
        invalidDto.setDescription("Description");
        invalidDto.setPriority(Priority.HIGH);
        invalidDto.setDueDate(LocalDate.now().plusDays(7));
        invalidDto.setTags("test");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, invalidDto, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void createTask_MissingPriority_ReturnsBadRequest() {
        TaskCreateDto invalidDto = new TaskCreateDto();
        invalidDto.setTitle("Valid Title");
        invalidDto.setDescription("Description");
        invalidDto.setDueDate(LocalDate.now().plusDays(7));
        invalidDto.setTags("test");

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, invalidDto, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void updateTask_ExistingId_ReturnsUpdatedTask() {
        TaskCreateDto createDto = createValidDto("Old Title");
        TaskResponseDto created = taskService.createTask(createDto);

        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle("Updated Title");
        updateDto.setDescription("Updated Description");
        updateDto.setCompleted(true);
        updateDto.setTags("updated");

        HttpEntity<TaskUpdateDto> request = new HttpEntity<>(updateDto);
        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            baseUrl + "/" + created.getId(),
            HttpMethod.PUT,
            request,
            TaskResponseDto.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Updated Title", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals("Updated Description", response.getBody().getDescription());
        assertTrue(response.getBody().isCompleted());
    }

    @Test
    void updateTask_NonExistingId_ReturnsNotFound() {
        TaskUpdateDto updateDto = new TaskUpdateDto();
        updateDto.setTitle("Title");
        updateDto.setTags("test");

        HttpEntity<TaskUpdateDto> request = new HttpEntity<>(updateDto);
        ResponseEntity<TaskResponseDto> response = restTemplate.exchange(
            baseUrl + "/999",
            HttpMethod.PUT,
            request,
            TaskResponseDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteTask_ExistingId_ReturnsNoContent() {
        TaskCreateDto dto = createValidDto("To Delete");
        TaskResponseDto created = taskService.createTask(dto);

        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/" + created.getId(),
            HttpMethod.DELETE,
            null,
            Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

        ResponseEntity<TaskResponseDto> getResponse = restTemplate.getForEntity(
            baseUrl + "/" + created.getId(), TaskResponseDto.class);
        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    void deleteTask_NonExistingId_ReturnsNotFound() {
        ResponseEntity<Void> response = restTemplate.exchange(
            baseUrl + "/999",
            HttpMethod.DELETE,
            null,
            Void.class
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    private TaskCreateDto createValidDto(String title) {
        TaskCreateDto dto = new TaskCreateDto();
        dto.setTitle(title);
        dto.setDescription("Description for " + title);
        dto.setPriority(Priority.HIGH);
        dto.setDueDate(LocalDate.now().plusDays(7));
        dto.setTags("test,urgent");
        return dto;
    }
}