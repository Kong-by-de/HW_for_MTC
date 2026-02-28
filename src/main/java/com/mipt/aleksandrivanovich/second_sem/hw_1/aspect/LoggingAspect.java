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

@Aspect
@Component
public class LoggingAspect {

  private static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);
  private static final String LOG_DIR = "logs/service";

  @Around("within(com.mipt.aleksandrivanovich.second_sem.hw_1.service..*)")
  public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
    String methodName = joinPoint.getSignature().getName();
    String className = joinPoint.getTarget().getClass().getSimpleName();
    Object[] args = joinPoint.getArgs();

    String startMessage = String.format("▶️ START: %s.%s(%s)",
        className, methodName, formatArgs(args));
    logger.info(startMessage);
    logToFile(startMessage);

    long startTime = System.currentTimeMillis();
    Object result = null;
    String status = "SUCCESS";

    try {
      result = joinPoint.proceed();
      return result;
    } catch (Throwable e) {
      status = "ERROR";
      logger.error("❌ ERROR in {}.{}: {}", className, methodName, e.getMessage());
      throw e;
    } finally {
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

  private String formatResult(Object result) {
    if (result == null) {
      return "null";
    }
    String resultStr = result.toString();
    return resultStr.length() > 100 ? resultStr.substring(0, 100) + "..." : resultStr;
  }

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