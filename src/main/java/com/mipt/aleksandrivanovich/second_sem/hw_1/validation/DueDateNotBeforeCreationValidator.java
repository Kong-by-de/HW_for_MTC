package com.mipt.aleksandrivanovich.second_sem.hw_1.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Реализация валидатора.
 */
public class DueDateNotBeforeCreationValidator implements ConstraintValidator<DueDateNotBeforeCreation, Object> {

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            LocalDate dueDate = null;
            LocalDateTime createdAt = null;

            if (value.getClass().getSimpleName().equals("TaskUpdateDto")) {
                dueDate = (LocalDate) value.getClass().getMethod("getDueDate").invoke(value);
                if (dueDate == null) {
                    return true;
                }

                return true;
            }

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}