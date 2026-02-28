package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class PrototypeScopedBean {

  private static final Logger logger = LoggerFactory.getLogger(PrototypeScopedBean.class);

  private final String instanceId;

  public PrototypeScopedBean() {
    this.instanceId = UUID.randomUUID().toString();
    logger.info("🔄 PrototypeScopedBean создан: {}", instanceId);
  }

  public String generateTaskId() {
    String taskId = UUID.randomUUID().toString();
    logger.info("Generated task ID: {} (from instance: {})", taskId, instanceId);
    return taskId;
  }

  public String getInstanceId() {
    return instanceId;
  }
}