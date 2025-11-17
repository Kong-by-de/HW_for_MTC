package com.mipt.aleksandrivanovich.Hw_10;

import java.util.Optional;

public class LoggingDecorator implements DataService {

  private final DataService wrappedService;

  public LoggingDecorator(DataService wrappedService) {
    this.wrappedService = wrappedService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    System.out.println("[Log] Finding data by key: " + key);
    Optional<String> result = wrappedService.findDataByKey(key);
    System.out.println("[Log] Found: " + result.orElse("null"));
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    System.out.println("[Log] Saving data with key: " + key + ", data: " + data);
    wrappedService.saveData(key, data);
    System.out.println("[Log] Saved successfully");
  }

  @Override
  public boolean deleteData(String key) {
    System.out.println("[Log] Deleting data by key: " + key);
    boolean result = wrappedService.deleteData(key);
    System.out.println("[Log] Deleted: " + result);
    return result;
  }
}