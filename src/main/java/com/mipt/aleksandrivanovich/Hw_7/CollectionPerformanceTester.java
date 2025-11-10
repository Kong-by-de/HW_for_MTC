package com.mipt.aleksandrivanovich.Hw_7;

import java.util.*;

public class CollectionPerformanceTester {

  private static final int N = 10_000;

  public static void main(String[] args) {
    System.out.printf("%-25s | %-15s | %-15s%n", "Операция", "ArrayList (ms)", "LinkedList (ms)");
    System.out.println("-".repeat(60));

    // Создаём коллекции
    List<Integer> arrayList = new ArrayList<>();
    List<Integer> linkedList = new LinkedList<>();

    // 1. Добавление в конец
    long start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      arrayList.add(i);
    }
    long time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      linkedList.add(i);
    }
    long time2 = System.currentTimeMillis() - start;

    printResult("Добавление в конец", time1, time2);

    // 2. Добавление в начало
    arrayList.clear();
    linkedList.clear();

    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      arrayList.add(0, i);
    }
    time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      linkedList.add(0, i);
    }
    time2 = System.currentTimeMillis() - start;

    printResult("Добавление в начало", time1, time2);

    // 3. Вставка в середину
    arrayList.clear();
    linkedList.clear();

    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      arrayList.add(arrayList.size() / 2, i);
    }
    time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      linkedList.add(linkedList.size() / 2, i);
    }
    time2 = System.currentTimeMillis() - start;

    printResult("Вставка в середину", time1, time2);

    // 4. Доступ по индексу
    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      arrayList.get(i % arrayList.size());
    }
    time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    for (int i = 0; i < N; i++) {
      linkedList.get(i % linkedList.size());
    }
    time2 = System.currentTimeMillis() - start;

    printResult("Доступ по индексу", time1, time2);

    // 5. Удаление из начала
    arrayList.clear();
    linkedList.clear();

    for (int i = 0; i < N; i++) {
      arrayList.add(i);
      linkedList.add(i);
    }

    start = System.currentTimeMillis();
    while (!arrayList.isEmpty()) {
      arrayList.remove(0);
    }
    time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    while (!linkedList.isEmpty()) {
      linkedList.remove(0);
    }
    time2 = System.currentTimeMillis() - start;

    printResult("Удаление из начала", time1, time2);

    // 6. Удаление из конца
    for (int i = 0; i < N; i++) {
      arrayList.add(i);
      linkedList.add(i);
    }

    start = System.currentTimeMillis();
    while (!arrayList.isEmpty()) {
      arrayList.remove(arrayList.size() - 1);
    }
    time1 = System.currentTimeMillis() - start;

    start = System.currentTimeMillis();
    while (!linkedList.isEmpty()) {
      linkedList.remove(linkedList.size() - 1);
    }
    time2 = System.currentTimeMillis() - start;

    printResult("Удаление из конца", time1, time2);
  }

  private static void printResult(String operation, long time1, long time2) {
    System.out.printf("%-25s | %-15d | %-15d%n", operation, time1, time2);
  }
}