package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Бин с областью видимости request.
 * Создается новый экземпляр для каждого HTTP-запроса.
 */
public class RequestScopedBean {

  private static final Logger logger = LoggerFactory.getLogger(RequestScopedBean.class);

  /**
   * Уникальный идентификатор запроса.
   */
  private final String requestId;

  /**
   * Время начала обработки запроса.
   */
  private final LocalDateTime requestStartTime;

  /**
   * Конструктор, вызываемый при создании бина для каждого запроса.
   * Генерирует requestId и фиксирует время создания.
   */
  public RequestScopedBean() {
    this.requestId = UUID.randomUUID().toString();
    this.requestStartTime = LocalDateTime.now();
    logger.info(" RequestScopedBean создан для запроса: {}", requestId);
  }

  /**
   * Возвращает уникальный идентификатор запроса.
   *
   * @return requestId в формате UUID
   */
  public String getRequestId() {
    return requestId;
  }

  /**
   * Возвращает время начала обработки запроса.
   *
   * @return LocalDateTime момента создания бина
   */
  public LocalDateTime getRequestStartTime() {
    return requestStartTime;
  }

  /**
   * Возвращает строковое описание запроса.
   *
   * @return строка с requestId и временем начала
   */
  public String getRequestInfo() {
    return String.format("Request ID: %s, Started at: %s",
        requestId, requestStartTime);
  }
}