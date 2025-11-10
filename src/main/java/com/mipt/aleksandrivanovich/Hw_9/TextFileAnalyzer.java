package com.mipt.aleksandrivanovich.Hw_9;

import java.io.*;
import java.util.StringTokenizer;

class TextFileAnalyzer {

  public static class AnalysisResult {
    private final long lineCount;
    private final long wordCount;
    private final long charCount;

    public AnalysisResult(long lineCount, long wordCount, long charCount) {
      this.lineCount = lineCount;
      this.wordCount = wordCount;
      this.charCount = charCount;
    }

    public long getLineCount() { return lineCount; }
    public long getWordCount() { return wordCount; }
    public long getCharCount() { return charCount; }

    @Override
    public String toString() {
      return "AnalysisResult{" +
          "lineCount=" + lineCount +
          ", wordCount=" + wordCount +
          ", charCount=" + charCount +
          '}';
    }
  }

  public AnalysisResult analyzeFile(String filePath) throws IOException {
    long lineCount = 0;
    long wordCount = 0;
    long charCount = 0;

    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        lineCount++;
        charCount += line.length(); // Символы в строке (без перевода строки)

        // Подсчёт слов через StringTokenizer
        if (!line.trim().isEmpty()) {
          StringTokenizer tokenizer = new StringTokenizer(line);
          wordCount += tokenizer.countTokens();
        }
      }
    }

    return new AnalysisResult(lineCount, wordCount, charCount);
  }
}