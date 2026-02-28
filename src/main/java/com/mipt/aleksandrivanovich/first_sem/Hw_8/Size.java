package com.mipt.aleksandrivanovich.first_sem.Hw_8;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Size {
  int min() default 0;
  int max() default Integer.MAX_VALUE;
  String message() default "Строка должна быть в пределах [min, max]";
}