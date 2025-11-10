package com.mipt.aleksandrivanovich.Hw_8;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class Validator {

  public static ValidationResult validate(Object object) {
    ValidationResult result = new ValidationResult();

    if (object == null) {
      result.addError("Объект не может быть null");
      return result;
    }

    Class<?> clazz = object.getClass();

    Field[] fields = clazz.getDeclaredFields();

    for (Field field : fields) {
      field.setAccessible(true);

      try {
        Object value = field.get(object);

        checkNotNull(field, value, result);
        checkSize(field, value, result);
        checkRange(field, value, result);
        checkEmail(field, value, result);

      } catch (IllegalAccessException e) {
        result.addError("Ошибка доступа к полю: " + field.getName());
      }
    }

    return result;
  }

  private static void checkNotNull(Field field, Object value, ValidationResult result) {
    NotNull notNull = field.getAnnotation(NotNull.class);
    if (notNull != null && value == null) {
      result.addError(notNull.message());
    }
  }

  private static void checkSize(Field field, Object value, ValidationResult result) {
    Size size = field.getAnnotation(Size.class);
    if (size != null) {
      if (value instanceof String str) {
        int len = str.length();
        if (len < size.min() || len > size.max()) {
          result.addError(size.message());
        }
      }
    }
  }

  private static void checkRange(Field field, Object value, ValidationResult result) {
    Range range = field.getAnnotation(Range.class);
    if (range != null) {
      if (value instanceof Number num) {
        double d = num.doubleValue();
        if (d < range.min() || d > range.max()) {
          result.addError(range.message());
        }
      }
    }
  }

  private static void checkEmail(Field field, Object value, ValidationResult result) {
    Email email = field.getAnnotation(Email.class);
    if (email != null) {
      if (value instanceof String str) {
        if (!isValidEmail(str)) {
          result.addError(email.message());
        }
      }
    }
  }

  private static boolean isValidEmail(String email) {
    if (email == null || email.trim().isEmpty()) {
      return false;
    }
    String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
    return Pattern.matches(emailRegex, email);
  }
}