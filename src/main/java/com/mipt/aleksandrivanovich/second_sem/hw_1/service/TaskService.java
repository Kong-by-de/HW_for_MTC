package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.config.TaskMapper;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskCreateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.TaskUpdateDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Сервис для управления задачами.
 * Содержит бизнес-логику приложения и кэширование задач.
 */
@Service
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    private final Map<String, Task> taskCache = new ConcurrentHashMap<>();

    /**
     * Конструктор с инжекцией репозитория и маппера.
     */
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
        logger.info("TaskService создан через конструктор");
    }

    /**
     * Метод инициализации, вызывается после создания бина.
     * Загружает задачи из репозитория в кэш при старте приложения.
     */
    @PostConstruct
    public void init() {
        logger.info("@PostConstruct: Инициализация TaskService и загрузка кэша...");
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            taskCache.put(task.getId(), task);
        }
        logger.info("Кэш загружен: {} задач", taskCache.size());
    }

    /**
     * Метод очистки, вызывается перед уничтожением бина.
     * Логирует статистику и освобождает ресурсы кэша.
     */
    @PreDestroy
    public void cleanup() {
        logger.info("@PreDestroy: Очистка ресурсов TaskService...");
        logger.info("В кэше осталось задач: {}", taskCache.size());
        taskCache.clear();
        logger.info("Ресурсы освобождены");
    }

    /**
     * Создаёт новую задачу из DTO.
     */
    public TaskResponseDto createTask(TaskCreateDto dto) {
        Task task = taskMapper.toEntity(dto);
        task.setId(UUID.randomUUID().toString());
        task.setCompleted(false);
        task.setCreatedAt(java.time.LocalDateTime.now());

        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDto(savedTask);
    }

    /**
     * Получает задачу по ID как ResponseDto.
     */
    public Optional<TaskResponseDto> getTaskById(String id) {
        return taskRepository.findById(id).map(taskMapper::toResponseDto);
    }

    /**
     * Возвращает все задачи как список ResponseDto.
     */
    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(taskMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    /**
     * Обновляет задачу из DTO.
     */
    public Optional<TaskResponseDto> updateTask(String id, TaskUpdateDto dto) {
        return taskRepository.findById(id).map(existingTask -> {
            Task updatedTask = taskMapper.updateEntity(dto, existingTask);
            updatedTask.setId(id);
            taskRepository.save(updatedTask);
            return taskMapper.toResponseDto(updatedTask);
        });
    }

    /**
     * Удаляет задачу по ID.
     */
    public boolean deleteTask(String id) {
        return taskRepository.deleteById(id);
    }

    /**
     * Помечает задачу как выполненную.
     */
    public boolean completeTask(String id) {
        return taskRepository.findById(id)
            .map(task -> {
                task.setCompleted(true);
                taskRepository.save(task);
                return true;
            })
            .orElse(false);
    }
}