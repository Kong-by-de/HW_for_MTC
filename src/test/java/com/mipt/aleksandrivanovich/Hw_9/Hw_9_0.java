package com.mipt.aleksandrivanovich.Hw_9;

import org.junit.jupiter.api.*;
import java.nio.file.*;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

public class Hw_9_0 {

  private TextFileAnalyzer analyzer;

  @BeforeEach
  void setUp() {
    analyzer = new TextFileAnalyzer();
  }

  @Test
  void testAnalyzeFile() throws IOException {
    Path testFile = Files.createTempFile("test", ".txt");
    Files.write(testFile, "Hello world!\nThis is a test.".getBytes());

    TextFileAnalyzer.AnalysisResult result = analyzer.analyzeFile(testFile.toString());

    assertEquals(2, result.getLineCount());
    assertEquals(6, result.getWordCount());
    assertEquals(27, result.getCharCount());

    // Удаляем временный файл
    Files.deleteIfExists(testFile);
  }

  @Test
  void testSaveAnalysisResult() throws IOException {
    TextFileAnalyzer.AnalysisResult result = new TextFileAnalyzer.AnalysisResult(2, 6, 27);

    Path outputFile = Files.createTempFile("analysis", ".txt");

    analyzer.saveAnalysisResult(result, outputFile.toString());

    assertTrue(Files.exists(outputFile));
    assertTrue(Files.size(outputFile) > 0);

    String content = Files.readString(outputFile);
    assertTrue(content.contains("Строк: 2"));
    assertTrue(content.contains("Слов: 6"));
    assertTrue(content.contains("Символов: 27"));

    Files.deleteIfExists(outputFile);
  }
}
