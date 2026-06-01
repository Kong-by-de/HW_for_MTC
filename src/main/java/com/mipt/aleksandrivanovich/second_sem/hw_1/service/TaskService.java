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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true) // По умолчанию транзакции только для чтения
public class TaskService {

    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    @PostConstruct
    public void init() {
        logger.info("TaskService initialized");
    }

    @PreDestroy
    public void cleanup() {
        logger.info("TaskService cleanup");
    }

    @Transactional // Для записи нужна активная транзакция
    public TaskResponseDto createTask(TaskCreateDto dto) {
        Task task = taskMapper.toEntity(dto);
        task.setCompleted(false);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toResponseDto(savedTask);
    }

    public Optional<TaskResponseDto> getTaskById(Long id) {
        return taskRepository.findById(id).map(taskMapper::toResponseDto);
    }

    public List<TaskResponseDto> getAllTasks() {
        return taskRepository.findAll().stream()
            .map(taskMapper::toResponseDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public Optional<TaskResponseDto> updateTask(Long id, TaskUpdateDto dto) {
        return taskRepository.findById(id).map(existingTask -> {
            Task updatedTask = taskMapper.updateEntity(dto, existingTask);
            taskRepository.save(updatedTask);
            return taskMapper.toResponseDto(updatedTask);
        });
    }

    @Transactional
    public boolean deleteTask(Long id) {
        if (taskRepository.existsById(id)) {
            taskRepository.deleteById(id);
            return true;
        }
        return false;
    }

    /**
     * Демонстрация транзакций:
     */
    @Transactional(rollbackFor = Exception.class)
    public void bulkCompleteTasks(List<Long> ids) {
        logger.info("Начинаю пакетное обновление для ID: {}", ids);

        for (Long id : ids) {
            if (!taskRepository.existsById(id)) {
                logger.error("Задача с ID {} не найдена. Откат транзакции!", id);
                throw new RuntimeException("Task not found with id: " + id);
            }
        }

        for (Long id : ids) {
            taskRepository.findById(id).ifPresent(task -> {
                task.setCompleted(true);
                taskRepository.save(task);
            });
        }
        logger.info("Пакетное обновление успешно завершено");
    }

    /**
     * Решение проблемы N+1.
     */
    public List<TaskResponseDto> getTasksWithAttachments(boolean completed) {
        List<Task> tasks = taskRepository.findAllWithAttachmentsByCompleted(completed);
        return tasks.stream()
            .map(taskMapper::toResponseDto)
            .collect(Collectors.toList());
    }
}