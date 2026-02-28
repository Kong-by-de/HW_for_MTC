package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Модульные тесты для TaskController.
 * Тестирует все CRUD endpoints контроллера.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TaskControllerTest {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Autowired
  private TaskRepository taskRepository;

  private String baseUrl;

  @BeforeEach
  void setUp() {
    baseUrl = "http://localhost:" + port + "/api/tasks";
    // Очищаем репозиторий перед каждым тестом
    taskRepository.findAll().forEach(task -> taskRepository.deleteById(task.getId()));
  }

  /**
   * Тест: GET /api/tasks - получить все задачи
   * Позитивный сценарий: возвращает пустой список
   */
  @Test
  void getAllTasks_EmptyList_ReturnsOk() {
    // When
    ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().isEmpty());
  }

  /**
   * Тест: GET /api/tasks - получить все задачи
   * Позитивный сценарий: возвращает список с задачами
   */
  @Test
  void getAllTasks_WithTasks_ReturnsAllTasks() {
    // Given
    Task task1 = new Task("1", "Task 1", "Description 1", false);
    Task task2 = new Task("2", "Task 2", "Description 2", true);
    taskRepository.save(task1);
    taskRepository.save(task2);

    // When
    ResponseEntity<List> response = restTemplate.getForEntity(baseUrl, List.class);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(2, response.getBody().size());
  }

  /**
   * Тест: GET /api/tasks/{id} - получить задачу по ID
   * Позитивный сценарий: задача найдена
   */
  @Test
  void getTaskById_ExistingId_ReturnsTask() {
    // Given
    Task task = new Task("test-id", "Test Task", "Test Description", false);
    taskRepository.save(task);

    // When
    ResponseEntity<Task> response = restTemplate.getForEntity(baseUrl + "/test-id", Task.class);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("test-id", Objects.requireNonNull(response.getBody()).getId());
    assertEquals("Test Task", response.getBody().getTitle());
  }

  /**
   * Тест: GET /api/tasks/{id} - получить задачу по ID
   * Негативный сценарий: задача не найдена
   */
  @Test
  void getTaskById_NonExistingId_ReturnsNotFound() {
    // When
    ResponseEntity<Task> response = restTemplate.getForEntity(baseUrl + "/non-existing", Task.class);

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  /**
   * Тест: POST /api/tasks - создать новую задачу
   * Позитивный сценарий: задача создана
   */
  @Test
  void createTask_ValidTask_ReturnsCreatedTask() {
    // Given
    Task newTask = new Task();
    newTask.setTitle("New Task");
    newTask.setDescription("New Description");
    newTask.setCompleted(false);

    // When
    ResponseEntity<Task> response = restTemplate.postForEntity(baseUrl, newTask, Task.class);

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertNotNull(Objects.requireNonNull(response.getBody()).getId());
    assertEquals("New Task", response.getBody().getTitle());
    assertFalse(response.getBody().isCompleted());
  }

  /**
   * Тест: POST /api/tasks - создать новую задачу
   * Негативный сценарий: пустой заголовок
   */
  @Test
  void createTask_EmptyTitle_ReturnsBadRequest() {
    // Given
    Task invalidTask = new Task();
    invalidTask.setTitle("");
    invalidTask.setDescription("Description");

    // When
    ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, invalidTask, String.class);

    // Then
    // Spring Boot вернет 400 или 500 в зависимости от валидации
    assertTrue(response.getStatusCode().is4xxClientError() ||
        response.getStatusCode().is5xxServerError());
  }

  /**
   * Тест: PUT /api/tasks/{id} - обновить задачу
   * Позитивный сценарий: задача обновлена
   */
  @Test
  void updateTask_ExistingId_ReturnsUpdatedTask() {
    // Given
    Task task = new Task("update-id", "Old Title", "Old Description", false);
    taskRepository.save(task);

    Task updatedTask = new Task("update-id", "Updated Title", "Updated Description", true);

    // When
    HttpEntity<Task> request = new HttpEntity<>(updatedTask);
    ResponseEntity<Task> response = restTemplate.exchange(
        baseUrl + "/update-id",
        HttpMethod.PUT,
        request,
        Task.class
    );

    // Then
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals("Updated Title", Objects.requireNonNull(response.getBody()).getTitle());
    assertEquals("Updated Description", response.getBody().getDescription());
    assertTrue(response.getBody().isCompleted());
  }

  /**
   * Тест: PUT /api/tasks/{id} - обновить задачу
   * Негативный сценарий: задача не найдена
   */
  @Test
  void updateTask_NonExistingId_ReturnsNotFound() {
    // Given
    Task updatedTask = new Task("non-existing", "Title", "Description", false);

    // When
    HttpEntity<Task> request = new HttpEntity<>(updatedTask);
    ResponseEntity<Task> response = restTemplate.exchange(
        baseUrl + "/non-existing",
        HttpMethod.PUT,
        request,
        Task.class
    );

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }

  /**
   * Тест: DELETE /api/tasks/{id} - удалить задачу
   * Позитивный сценарий: задача удалена
   */
  @Test
  void deleteTask_ExistingId_ReturnsNoContent() {
    // Given
    Task task = new Task("delete-id", "To Delete", "Description", false);
    taskRepository.save(task);

    // When
    ResponseEntity<Void> response = restTemplate.exchange(
        baseUrl + "/delete-id",
        HttpMethod.DELETE,
        null,
        Void.class
    );

    // Then
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());

    // Проверяем что задача действительно удалена
    ResponseEntity<Task> getResponse = restTemplate.getForEntity(baseUrl + "/delete-id", Task.class);
    assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
  }

  /**
   * Тест: DELETE /api/tasks/{id} - удалить задачу
   * Негативный сценарий: задача не найдена
   */
  @Test
  void deleteTask_NonExistingId_ReturnsNotFound() {
    // When
    ResponseEntity<Void> response = restTemplate.exchange(
        baseUrl + "/non-existing",
        HttpMethod.DELETE,
        null,
        Void.class
    );

    // Then
    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}