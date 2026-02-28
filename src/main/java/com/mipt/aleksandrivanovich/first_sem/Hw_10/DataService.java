package com.mipt.aleksandrivanovich.first_sem.Hw_6.Hw_10;

import java.util.Optional;

public interface DataService {
  Optional<String> findDataByKey(String key);
  void saveData(String key, String data);
  boolean deleteData(String key);
}