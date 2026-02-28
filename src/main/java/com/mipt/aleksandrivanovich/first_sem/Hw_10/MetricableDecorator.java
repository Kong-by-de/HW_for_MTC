package com.mipt.aleksandrivanovich.first_sem.Hw_10;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class MetricableDecorator implements DataService {

  private final DataService wrappedService;

  public MetricableDecorator(DataService wrappedService) {
    this.wrappedService = wrappedService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    Instant start = Instant.now();
    Optional<String> result = wrappedService.findDataByKey(key);
    Duration duration = Duration.between(start, Instant.now());
    MetricService.sendMetric(duration);
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    Instant start = Instant.now();
    wrappedService.saveData(key, data);
    Duration duration = Duration.between(start, Instant.now());
    MetricService.sendMetric(duration);
  }

  @Override
  public boolean deleteData(String key) {
    Instant start = Instant.now();
    boolean result = wrappedService.deleteData(key);
    Duration duration = Duration.between(start, Instant.now());
    MetricService.sendMetric(duration);
    return result;
  }

  public static class MetricService {
    public static void sendMetric(Duration duration) {
      System.out.println("Метод выполнился за: " + duration.toMillis() + " мс");
    }
  }
}