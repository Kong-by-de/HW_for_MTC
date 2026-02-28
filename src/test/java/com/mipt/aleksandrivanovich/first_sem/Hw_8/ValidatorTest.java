package com.mipt.aleksandrivanovich.Hw_8;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ValidatorTest {

  private Validator validator;

  @BeforeEach
  void setUp() {
    validator = new Validator(); // Хотя метод static — можно и без создания экземпляра
  }

  @Test
  void testValidUser() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(25);
    user.setPassword("password123");

    ValidationResult result = Validator.validate(user);

    assertTrue(result.isValid());
    assertTrue(result.getErrors().isEmpty());
  }

  @Test
  void testNameIsNull() {
    User user = new User();
    user.setEmail("john@example.com");
    user.setAge(25);
    user.setPassword("password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertEquals(1, result.getErrors().size());
    assertTrue(result.getErrors().get(0).contains("Имя не может быть null"));
  }

  @Test
  void testNameTooShort() {
    User user = new User();
    user.setName("A"); // меньше 2 символов
    user.setEmail("john@example.com");
    user.setAge(25);
    user.setPassword("password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(e -> e.contains("Имя должно быть от 2 до 50 символов")));
  }

  @Test
  void testInvalidEmail() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("invalid-email"); // невалидный email
    user.setAge(25);
    user.setPassword("password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(e -> e.contains("Некорректный формат email")));
  }

  @Test
  void testAgeOutOfRange() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(200); // больше 150
    user.setPassword("password123");

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(e -> e.contains("Возраст должен быть от 0 до 150")));
  }

  @Test
  void testPasswordTooLong() {
    User user = new User();
    user.setName("John Doe");
    user.setEmail("john@example.com");
    user.setAge(25);
    user.setPassword("a".repeat(21)); // 21 символ — больше 20

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertTrue(result.getErrors().stream()
        .anyMatch(e -> e.contains("Пароль должен быть от 6 до 20 символов")));
  }

  @Test
  void testAllErrors() {
    User user = new User();
    user.setName(""); // пустая строка — не проходит @Size(min=2)
    user.setEmail(null); // null — не проходит @NotNull и @Email
    user.setAge(-5); // меньше 0 — не проходит @Range
    user.setPassword("short"); // 5 символов — меньше 6

    ValidationResult result = Validator.validate(user);

    assertFalse(result.isValid());
    assertEquals(4, result.getErrors().size()); // 4 ошибки
  }
}