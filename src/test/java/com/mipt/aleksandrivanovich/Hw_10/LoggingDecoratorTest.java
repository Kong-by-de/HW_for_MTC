package com.mipt.aleksandrivanovich.Hw_10;

import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import static org.junit.jupiter.api.Assertions.*;

class LoggingDecoratorTest {

  @Test
  void saveData_shouldLogAction() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    DataService service = new LoggingDecorator(new SimpleDataService());
    service.saveData("key", "data");

    String output = outContent.toString();
    assertTrue(output.contains("[Log] Saving data with key: key, data: data"));
    assertTrue(output.contains("[Log] Saved successfully"));

    System.setOut(System.out);
  }

  @Test
  void findDataByKey_shouldLogFoundValue() {
    ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    System.setOut(new PrintStream(outContent));

    DataService service = new LoggingDecorator(new SimpleDataService());
    service.saveData("key", "data");
    service.findDataByKey("key");

    String output = outContent.toString();
    assertTrue(output.contains("[Log] Finding data by key: key"));
    assertTrue(output.contains("[Log] Found: data"));

    System.setOut(System.out);
  }
}