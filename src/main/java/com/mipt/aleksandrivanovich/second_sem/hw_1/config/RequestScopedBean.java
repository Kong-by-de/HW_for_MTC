package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

public class RequestScopedBean {

  private static final Logger logger = LoggerFactory.getLogger(RequestScopedBean.class);

  private final String requestId;
  private final LocalDateTime requestStartTime;

  public RequestScopedBean() {
    this.requestId = UUID.randomUUID().toString();
    this.requestStartTime = LocalDateTime.now();
    logger.info(" RequestScopedBean создан для запроса: {}", requestId);
  }

  public String getRequestId() {
    return requestId;
  }

  public LocalDateTime getRequestStartTime() {
    return requestStartTime;
  }

  public String getRequestInfo() {
    return String.format("Request ID: %s, Started at: %s",
        requestId, requestStartTime);
  }
}