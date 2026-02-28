package com.mipt.aleksandrivanovich.Hw_10;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class MetricableDecoratorTest {

  @Test
  void allOperations_shouldOutputExecutionTime() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    DataService service = new MetricableDecorator(new SimpleDataService());

    service.saveData("key", "data");
    service.findDataByKey("key");
    service.deleteData("key");

    String output = outContent.toString();
    // Проверяем, что за каждую операцию была выведена метрика
    long count = output.lines()
        .filter(line -> line.startsWith("Метод выполнился за:"))
        .count();

    assertEquals(3, count);

    System.setOut(System.out);
  }
}