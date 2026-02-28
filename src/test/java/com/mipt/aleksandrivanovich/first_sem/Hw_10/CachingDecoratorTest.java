package com.mipt.aleksandrivanovich.Hw_10;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

class CachingDecoratorTest {

  private CachingDecorator cachingService;
  private SimpleDataService baseService;

  @BeforeEach
  void setUp() {
    baseService = new SimpleDataService();
    cachingService = new CachingDecorator(baseService);
  }

  @Test
  void findDataByKey_shouldReturnCachedValueOnSecondCall() {
    cachingService.saveData("key1", "value1");

    Optional<String> firstCall = cachingService.findDataByKey("key1");
    Optional<String> secondCall = cachingService.findDataByKey("key1");

    assertTrue(firstCall.isPresent());
    assertTrue(secondCall.isPresent());
    assertEquals("value1", firstCall.get());
    assertEquals("value1", secondCall.get());
  }

  @Test
  void deleteData_shouldInvalidateCache() {
    cachingService.saveData("key1", "value1");
    cachingService.findDataByKey("key1"); // кешируем

    boolean deleted = cachingService.deleteData("key1");
    Optional<String> afterDelete = cachingService.findDataByKey("key1");

    assertTrue(deleted);
    assertFalse(afterDelete.isPresent());
  }
}