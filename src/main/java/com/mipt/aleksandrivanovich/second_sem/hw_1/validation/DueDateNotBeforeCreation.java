package com.mipt.aleksandrivanovich.second_sem.hw_1.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Валидатор для проверки, что dueDate не раньше даты создания.
 */
@Documented
@Constraint(validatedBy = DueDateNotBeforeCreationValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DueDateNotBeforeCreation {

    String message() default "Due date cannot be before creation date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}