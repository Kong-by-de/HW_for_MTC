package com.mipt.aleksandrivanovich.Hw_9;

import java.io.*;
import java.util.StringTokenizer;

public class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;

    public AnalysisResult(long lineCount, long wordCount, long charCount) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
    }

    public long getLineCount() {return lineCount;}
    public long getWordCount() {return wordCount;}
    public long getCharCount() {return charCount;}

    @Override
    public String toString() {
      return "AnalysisResult{" +
          "lineCount=" + lineCount +
          ", wordCount=" + wordCount +
          ", charCount=" + charCount +
          '}';
    }
  }

  /**
   * Анализирует текстовый файл и возвращает результаты подсчёта строк, слов и символов.
   *
   * @param filePath путь к анализируемому файлу
   * @return результат анализа
   * @throws IOException если файл не найден или возникла ошибка ввода-вывода
   */
  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length();

        // Подсчёт слов: пропускаем пустые строки
        if (!line.trim().isEmpty()) {
          StringTokenizer tokenizer = new StringTokenizer(line);
          wordCount += tokenizer.countTokens();
        }
      }
    }

    return new AnalysisResult(lineCount, wordCount, charCount);
  }

  /**
   * Сохраняет результат анализа в текстовый файл.
   *
   * @param result результат анализа
   * @param outputPath путь к файлу для сохранения
   * @throws IOException если возникла ошибка при записи
   */
  public void saveAnalysisResult(AnalysisResult result, String outputPath) throws IOException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
      writer.write("Строк: " + result.getLineCount());
      writer.newLine();
      writer.write("Слов: " + result.getWordCount());
      writer.newLine();
      writer.write("Символов: " + result.getCharCount());
      writer.newLine();
    }
  }
}