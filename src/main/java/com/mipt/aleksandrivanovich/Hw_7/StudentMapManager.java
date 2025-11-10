package com.mipt.aleksandrivanovich.Hw_7;

import java.util.*;

public class StudentMapManager {

  // Метод 1: найти студентов по диапазону оценок
  public static List<Student> findStudentsByGradeRange(Map<Integer, Student> map, double minGrade, double maxGrade) {
    List<Student> result = new ArrayList<>();
    for (Student student : map.values()) {
      if (student.getGrade() >= minGrade && student.getGrade() <= maxGrade) {
        result.add(student);
      }
    }
    return result;
  }

  // Метод 2: получить top N студентов по id (в порядке убывания)
  public static List<Student> getTopNStudents(TreeMap<Integer, Student> map, int n) {
    List<Student> result = new ArrayList<>();
    int count = 0;
    // TreeMap по умолчанию сортирует по возрастанию ключа
    // Чтобы получить по убыванию — используем descendingKeySet()
    for (Integer key : map.descendingKeySet()) {
      if (count >= n) break;
      result.add(map.get(key));
      count++;
    }
    return result;
  }

  // Пример использования
  public static void main(String[] args) {
    // Создаём HashMap
    Map<Integer, Student> hashMap = new HashMap<>();
    hashMap.put(1, new Student(1, "Alice", 85.5));
    hashMap.put(2, new Student(2, "Bob", 92.0));
    hashMap.put(3, new Student(3, "Charlie", 78.0));

    // Поиск по диапазону
    List<Student> range = findStudentsByGradeRange(hashMap, 80.0, 95.0);
    System.out.println("Студенты с оценками от 80 до 95:");
    range.forEach(System.out::println);

    // Создаём TreeMap с сортировкой по убыванию id
    TreeMap<Integer, Student> treeMap = new TreeMap<>(Comparator.reverseOrder());
    treeMap.putAll(hashMap);

    // Получаем top 2 по id
    List<Student> top2 = getTopNStudents(treeMap, 2);
    System.out.println("\nТоп 2 студента по id (убывание):");
    top2.forEach(System.out::println);
  }
}