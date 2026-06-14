package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.client.ExternalTasksClient;
import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.ExternalTaskDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.exception.ExternalApiException;
import com.mipt.aleksandrivanovich.second_sem.hw_1.exception.TaskNotFoundException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TasksGatewayService {

    private static final Logger logger = LoggerFactory.getLogger(TasksGatewayService.class);

    private final ExternalTasksClient externalTasksClient;

    public TasksGatewayService(ExternalTasksClient externalTasksClient) {
        this.externalTasksClient = externalTasksClient;
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTasksFallback")
    public List<ExternalTaskDto> getTasks(Boolean completed, int limit) {
        logger.debug("Fetching tasks with completed={}, limit={}", completed, limit);
        return externalTasksClient.getTasks(completed, limit);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "getTaskFallback")
    public ExternalTaskDto getTask(Long id) {
        logger.debug("Fetching task with id={}", id);
        return externalTasksClient.getTask(id);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "createTaskFallback")
    public ExternalTaskDto createTask(String title, boolean completed) {
        logger.debug("Creating task with title={}, completed={}", title, completed);
        return externalTasksClient.createTask(title, completed);
    }

    @RateLimiter(name = "externalApi")
    @CircuitBreaker(name = "externalApi", fallbackMethod = "deleteTaskFallback")
    public void deleteTask(Long id) {
        logger.debug("Deleting task with id={}", id);
        externalTasksClient.deleteTask(id);
    }

    public List<ExternalTaskDto> getTasksFallback(Boolean completed, int limit, Throwable t) {
        if (t instanceof RequestNotPermitted) {
            throw (RequestNotPermitted) t;
        }
        logger.warn("Circuit Breaker fallback for getTasks: {}", t.getMessage());
        throw new ExternalApiException("Service temporarily unavailable", t);
    }

    public ExternalTaskDto getTaskFallback(Long id, Throwable t) {
        if (t instanceof RequestNotPermitted) {
            throw new RuntimeException("Rate limit exceeded", t);
        }
        if (t instanceof TaskNotFoundException) {
            throw (TaskNotFoundException) t;
        }
        logger.warn("Circuit Breaker fallback for getTask({}): {}", id, t.getMessage());
        throw new ExternalApiException("Service temporarily unavailable", t);
    }

    public ExternalTaskDto createTaskFallback(String title, boolean completed, Throwable t) {
        if (t instanceof RequestNotPermitted) {
            throw new RuntimeException("Rate limit exceeded", t);
        }
        logger.warn("Circuit Breaker fallback for createTask: {}", t.getMessage());
        throw new ExternalApiException("Service temporarily unavailable", t);
    }

    public void deleteTaskFallback(Long id, Throwable t) {
        if (t instanceof RequestNotPermitted) {
            throw new RuntimeException("Rate limit exceeded", t);
        }
        logger.warn("Circuit Breaker fallback for deleteTask({}): {}", id, t.getMessage());
        throw new ExternalApiException("Service temporarily unavailable", t);
    }
}