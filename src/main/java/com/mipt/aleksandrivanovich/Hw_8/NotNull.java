package com.mipt.aleksandrivanovich.Hw_8;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NotNull {
  String message() default "Поле не может быть null";
}