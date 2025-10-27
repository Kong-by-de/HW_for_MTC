package com.mipt.aleksandrivanovich.Hw_8;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Range {
  double min() default Double.MIN_VALUE;
  double max() default Double.MAX_VALUE;
  String message() default "Число должно быть в диапазоне [min, max]";
}