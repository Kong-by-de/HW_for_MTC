package com.mipt.aleksandrivanovich.second_sem.hw_1.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(String message) {
        super(message);
    }
}