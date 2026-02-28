package com.mipt.aleksandrivanovich.first_sem.Hw_10;

import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class ValidationDecoratorTest {

  @Test
  void saveData_shouldThrowExceptionWhenKeyIsNull() {
    DataService service = new ValidationDecorator(new SimpleDataService());
    assertThrows(IllegalArgumentException.class, () -> {
      service.saveData(null, "data");
    });
  }

  @Test
  void saveData_shouldThrowExceptionWhenKeyIsEmpty() {
    DataService service = new ValidationDecorator(new SimpleDataService());
    assertThrows(IllegalArgumentException.class, () -> {
      service.saveData("", "data");
    });
    assertThrows(IllegalArgumentException.class, () -> {
      service.saveData("   ", "data"); // только пробелы
    });
  }

  @Test
  void saveData_shouldThrowExceptionWhenDataIsNull() {
    DataService service = new ValidationDecorator(new SimpleDataService());
    assertThrows(IllegalArgumentException.class, () -> {
      service.saveData("key", null);
    });
  }

  @Test
  void findDataByKey_shouldThrowExceptionOnNullKey() {
    DataService service = new ValidationDecorator(new SimpleDataService());
    assertThrows(IllegalArgumentException.class, () -> {
      service.findDataByKey(null);
    });
  }

  @Test
  void deleteData_shouldThrowExceptionOnEmptyKey() {
    DataService service = new ValidationDecorator(new SimpleDataService());
    assertThrows(IllegalArgumentException.class, () -> {
      service.deleteData("");
    });
  }

  @Test
  void validOperations_shouldWorkNormally() {
    DataService service = new ValidationDecorator(new SimpleDataService());
    service.saveData("key", "value");
    Optional<String> result = service.findDataByKey("key");
    assertTrue(result.isPresent());
    assertEquals("value", result.get());
    assertTrue(service.deleteData("key"));
  }
}