package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

/**
 * Исключение, выбрасываемое когда задача не найдена.
 */
public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(String message) {
        super(message);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}