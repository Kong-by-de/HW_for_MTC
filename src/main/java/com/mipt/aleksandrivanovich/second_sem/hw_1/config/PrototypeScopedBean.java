package com.mipt.aleksandrivanovich.second_sem.hw_1.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Бин с областью видимости prototype.
 * <p>
 * Создается новый экземпляр при каждом обращении к {@link org.springframework.context.ApplicationContext#getBean}.
 * Используется как генератор уникальных идентификаторов для задач.
 * </p>
 * <p>
 * <b>Scope:</b> {@code @Scope("prototype")}
 * </p>
 * <p>
 * <b>Пример использования:</b>
 * <pre>{@code
 * @Autowired
 * private ApplicationContext context;
 *
 * public void createTask() {
 *     PrototypeScopedBean generator1 = context.getBean(PrototypeScopedBean.class);
 *     PrototypeScopedBean generator2 = context.getBean(PrototypeScopedBean.class);
 *     // generator1.getInstanceId() != generator2.getInstanceId()
 *     String taskId = generator1.generateTaskId();
 * }
 * }</pre>
 */
public class PrototypeScopedBean {

  /**
   * Логгер для вывода информационных сообщений о создании экземпляра и генерации ID.
   * Использует SLF4J с реализацией по умолчанию.
   */
  private static final Logger logger = LoggerFactory.getLogger(PrototypeScopedBean.class);

  /**
   * Уникальный идентификатор экземпляра бина.
   * <p>
   * Генерируется один раз при создании экземпляра с помощью {@link UUID#randomUUID()}.
   * Позволяет отличать разные экземпляры prototype-бина друг от друга.
   * </p>
   */
  private final String instanceId;

  /**
   * Конструктор без параметров.
   * <p>
   * Вызывается при каждом создании нового экземпляра бина.
   * Генерирует уникальный instanceId и логирует событие создания.
   * </p>
   */
  public PrototypeScopedBean() {
    this.instanceId = UUID.randomUUID().toString();
    logger.info("🔄 PrototypeScopedBean создан: {}", instanceId);
  }

  /**
   * Генерирует уникальный идентификатор для новой задачи.
   * <p>
   * Использует {@link UUID#randomUUID()} для создания UUID версии 4.
   * Логирует сгенерированный ID и идентификатор экземпляра, который его создал.
   * </p>
   *
   * @return строковое представление UUID в формате стандартного UUID (36 символов)
   *
   * <p><b>Пример возвращаемого значения:</b> {@code "550e8400-e29b-41d4-a716-446655440000"}</p>
   */
  public String generateTaskId() {
    String taskId = UUID.randomUUID().toString();
    logger.info("Generated task ID: {} (from instance: {})", taskId, instanceId);
    return taskId;
  }

  /**
   * Возвращает уникальный идентификатор экземпляра бина.
   * <p>
   * Позволяет отследить, какой именно экземпляр prototype-бина был использован.
   * Для каждого нового экземпляра возвращаемое значение будет уникальным.
   * </p>
   *
   * @return строковое представление instanceId, сгенерированное при создании экземпляра
   *
   * <p><b>Пример использования в тестах:</b>
   * <pre>{@code
   * PrototypeScopedBean bean1 = context.getBean(PrototypeScopedBean.class);
   * PrototypeScopedBean bean2 = context.getBean(PrototypeScopedBean.class);
   * assertNotEquals(bean1.getInstanceId(), bean2.getInstanceId());
   * }</pre>
   * </p>
   */
  public String getInstanceId() {
    return instanceId;
  }
}