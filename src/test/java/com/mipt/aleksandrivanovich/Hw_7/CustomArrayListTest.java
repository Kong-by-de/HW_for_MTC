package com.mipt.aleksandrivanovich;

import com.mipt.aleksandrivanovich.Hw_7.CustomArrayList; // ← Добавь эту строку вручную
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class CustomArrayListTest {

  private CustomArrayList<String> list;

  @BeforeEach
  void setUp() {
    list = new CustomArrayList<>();
  }

  @Test
  void testAddAndGet() {
    list.add("Hello");
    list.add("World");

    assertEquals("Hello", list.get(0));
    assertEquals("World", list.get(1));
  }

  @Test
  void testRemove() {
    list.add("A");
    list.add("B");
    list.add("C");

    String removed = list.remove(1); // Удаляем "B"
    assertEquals("B", removed);
    assertEquals("C", list.get(1)); // "C" сместился на место "B"
    assertEquals(2, list.size());
  }

  @Test
  void testSizeAndIsEmpty() {
    assertTrue(list.isEmpty());
    list.add("Item");
    assertFalse(list.isEmpty());
    assertEquals(1, list.size());
  }

  @Test
  void testGetThrowsException() {
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    list.add("Test");
    assertThrows(IndexOutOfBoundsException.class, () -> list.get(1));
  }

  @Test
  void testAddNullThrowsException() {
    assertThrows(IllegalArgumentException.class, () -> list.add(null));
  }

  @Test
  void testIterator() {
    list.add("One");
    list.add("Two");
    list.add("Three");

    int count = 0;
    for (String item : list) {
      count++;
    }
    assertEquals(3, count);
  }
}