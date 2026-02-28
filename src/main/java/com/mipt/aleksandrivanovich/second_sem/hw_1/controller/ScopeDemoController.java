package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.config.PrototypeScopedBean;
import com.mipt.aleksandrivanovich.second_sem.hw_1.config.RequestScopedBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Контроллер для демонстрации работы бинов с разными областями видимости.
 * Предоставляет эндпоинты для проверки request и prototype scope.
 */
@RestController
@RequestMapping("/api/scope")
public class ScopeDemoController {

  private final RequestScopedBean requestScopedBean;
  private final ApplicationContext applicationContext;

  /**
   * Конструктор с инжекцией зависимостей.
   *
   * @param requestScopedBean бин с request scope для демонстрации
   * @param applicationContext контекст для получения prototype-бинов
   */
  @Autowired
  public ScopeDemoController(RequestScopedBean requestScopedBean,
                             ApplicationContext applicationContext) {
    this.requestScopedBean = requestScopedBean;
    this.applicationContext = applicationContext;
  }

  /**
   * Возвращает информацию о request-scoped бине.
   * При каждом HTTP-запросе создается новый экземпляр, поэтому requestId будет разным.
   *
   * @return карта с requestId, временем создания и описанием запроса
   */
  @GetMapping("/request")
  public Map<String, String> getRequestScopeInfo() {
    Map<String, String> info = new HashMap<>();
    info.put("message", "Request-scoped bean demonstration");
    info.put("requestId", requestScopedBean.getRequestId());
    info.put("startTime", requestScopedBean.getRequestStartTime().toString());
    info.put("instance", requestScopedBean.getRequestInfo());
    return info;
  }

  /**
   * Демонстрирует работу prototype-scoped бина.
   * Получает два экземпляра из контекста и показывает, что они разные.
   *
   * @return карта с идентификаторами экземпляров, сгенерированными ID и флагом различия
   */
  @GetMapping("/prototype")
  public Map<String, Object> getPrototypeScopeInfo() {
    Map<String, Object> result = new HashMap<>();

    PrototypeScopedBean bean1 = applicationContext.getBean(PrototypeScopedBean.class);
    PrototypeScopedBean bean2 = applicationContext.getBean(PrototypeScopedBean.class);

    result.put("message", "Prototype-scoped bean demonstration");
    result.put("bean1_instance_id", bean1.getInstanceId());
    result.put("bean2_instance_id", bean2.getInstanceId());
    result.put("bean1_generated_id", bean1.generateTaskId());
    result.put("bean2_generated_id", bean2.generateTaskId());
    result.put("are_different", !bean1.getInstanceId().equals(bean2.getInstanceId()));

    return result;
  }
}