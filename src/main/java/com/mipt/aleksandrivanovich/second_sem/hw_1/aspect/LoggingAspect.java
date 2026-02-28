package com.mipt.aleksandrivanovich.second_sem.hw_1.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Аспект для логирования вызовов методов сервиса.
 * Использует @Around advice для перехвата вызовов методов в пакете service.
 * Логирует начало выполнения, конец выполнения, параметры, результаты и время выполнения.
 * Также сохраняет логи в файл в директории logs/service.
 *
 * <p>Пример использования:
 * <pre>
 * {@code
 * @Service
 * public class TaskService {
 *     // Все методы этого класса будут автоматически логироваться
 *     public Task createTask(String title, String description) { ... }
 * }
 * }
 * </pre>
 */
@Aspect
@Component
public class LoggingAspect {

  /**
   * Логгер для вывода сообщений в консоль.
   * Использует SLF4J с реализацией по умолчанию.
   */
  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

  /**
   * Директория для сохранения файлов логов.
   * Логи сохраняются в файл service-log.txt внутри этой директории.
   */
  private static final String LOG_DIR = "logs/service";

  /**
   * Перехватывает все методы в пакете service и его подпакетах.
   * Логирует начало и конец выполнения, параметры метода, результат и время выполнения.
   * В случае исключения логирует ошибку и пробрасывает её дальше.
   *
   * @param joinPoint точка соединения, содержащая информацию о вызываемом методе
   * @return результат выполнения оригинального метода
   * @throws Throwable если оригинальный метод выбросил исключение
   *
   * <p>Pointcut выражение: {@code within(com.mipt.aleksandrivanovich.second_sem.hw_1.service..*)}
   * означает "все классы в пакете service и его подпакетах".
   */
  @Around("within(com.mipt.aleksandrivanovich.second_sem.hw_1.service..*)")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    Object[] args = joinPoint.getArgs();

    // Логируем начало выполнения метода
    String startMessage = String.format("▶️ START: %s.%s(%s)",
        className, methodName, formatArgs(args));
    logger.info(startMessage);
    logToFile(startMessage);

    long startTime = System.currentTimeMillis();
    Object result = null;
    String status = "SUCCESS";

    try {
      // Вызываем оригинальный метод
      result = joinPoint.proceed();
      return result;
    } catch (Throwable e) {
      // Обработка исключения
      status = "ERROR";
      logger.error("❌ ERROR in {}.{}: {}", className, methodName, e.getMessage());
      throw e;
    } finally {
      // Логируем завершение метода независимо от результата
      long duration = System.currentTimeMillis() - startTime;

      String endMessage;
      if (result == null) {
        endMessage = String.format("⏹️ END: %s.%s | Status: %s | Duration: %d ms",
            className, methodName, status, duration);
      } else {
        endMessage = String.format("⏹️ END: %s.%s | Status: %s | Duration: %d ms | Result: %s",
            className, methodName, status, duration, formatResult(result));
      }
      logger.info(endMessage);
      logToFile(endMessage);
    }
  }

  /**
   * Форматирует аргументы метода для вывода в лог.
   * Преобразует массив объектов в строку, разделённую запятыми.
   *
   * @param args массив аргументов метода
   * @return строковое представление аргументов, или пустая строка если аргументов нет
   *
   * <p>Пример: {@code [arg1, arg2, null]} → {@code "arg1, arg2, null"}
   */
  private String formatArgs(Object[] args) {
    if (args == null || args.length == 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < args.length; i++) {
      if (i > 0) sb.append(", ");
      sb.append(args[i] != null ? args[i].toString() : "null");
    }
    return sb.toString();
  }

  /**
   * Форматирует результат выполнения метода для вывода в лог.
   * Если результат null — возвращает строку "null".
   * Если результат слишком длинный (>100 символов) — обрезает и добавляет "...".
   *
   * @param result результат выполнения метода
   * @return строковое представление результата, пригодное для логирования
   */
  private String formatResult(Object result) {
    if (result == null) {
      return "null";
    }
    String resultStr = result.toString();
    return resultStr.length() > 100 ? resultStr.substring(0, 100) + "..." : resultStr;
  }

  /**
   * Сохраняет сообщение лога в файл.
   * Создаёт директорию лога, если она не существует.
   * Добавляет сообщение в конец файла service-log.txt с временной меткой.
   *
   * @param message сообщение для записи в лог
   *
   * <p>Формат записи: {@code yyyy-MM-dd HH:mm:ss - message}
   *
   * <p>В случае ошибки записи выводит предупреждение в лог, но не прерывает выполнение.
   */
  private void logToFile(String message) {
    try {
      Path logDir = Paths.get(LOG_DIR);
      if (!Files.exists(logDir)) {
        Files.createDirectories(logDir);
      }

      Path logFile = logDir.resolve("service-log.txt");
      String timestamp = LocalDateTime.now()
          .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

      Files.write(logFile,
          (timestamp + " - " + message + "\n").getBytes(),
          StandardOpenOption.CREATE,
          StandardOpenOption.APPEND);
    } catch (Exception e) {
      logger.warn("Не удалось записать лог в файл: {}", e.getMessage());
    }
  }
}