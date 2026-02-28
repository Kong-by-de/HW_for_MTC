package com.mipt.aleksandrivanovich.first_sem.Hw_8;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Email {
  String message() default "Строка должна быть валидным email";
}