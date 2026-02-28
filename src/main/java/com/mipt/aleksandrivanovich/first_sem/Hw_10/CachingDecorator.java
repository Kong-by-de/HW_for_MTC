package com.mipt.aleksandrivanovich.first_sem.Hw_10;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class CachingDecorator implements DataService {

  private final DataService wrappedService;
  private final Map<String, String> cache = new HashMap<>();

  public CachingDecorator(DataService wrappedService) {
    this.wrappedService = wrappedService;
  }

  @Override
  public Optional<String> findDataByKey(String key) {
    if (cache.containsKey(key)) {
      System.out.println("[Cache] Hit for key: " + key);
      return Optional.of(cache.get(key));
    }

    Optional<String> result = wrappedService.findDataByKey(key);
    result.ifPresent(data -> cache.put(key, data));
    System.out.println("[Cache] Miss for key: " + key);
    return result;
  }

  @Override
  public void saveData(String key, String data) {
    wrappedService.saveData(key, data);
    cache.put(key, data);
    System.out.println("[Cache] Saved and cached key: " + key);
  }

  @Override
  public boolean deleteData(String key) {
    boolean deleted = wrappedService.deleteData(key);
    if (deleted) {
      cache.remove(key);
      System.out.println("[Cache] Invalidated key: " + key);
    }
    return deleted;
  }
}