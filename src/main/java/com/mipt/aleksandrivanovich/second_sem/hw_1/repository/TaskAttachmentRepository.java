package com.mipt.aleksandrivanovich.second_sem.hw_1.repository;

import com.mipt.aleksandrivanovich.second_sem.hw_1.model.TaskAttachment;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * In-memory репозиторий для вложений задач.
 */
@Component
public class TaskAttachmentRepository {

    private final Map<Long, TaskAttachment> storage = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public TaskAttachment save(TaskAttachment attachment) {
        if (attachment.getId() == null) {
            attachment.setId(idGenerator.getAndIncrement());
        }
        storage.put(attachment.getId(), attachment);
        return attachment;
    }

    public Optional<TaskAttachment> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<TaskAttachment> findByTaskId(String taskId) {
        List<TaskAttachment> attachments = new ArrayList<>();
        for (TaskAttachment attachment : storage.values()) {
            if (attachment.getTaskId().equals(taskId)) {
                attachments.add(attachment);
            }
        }
        return attachments;
    }

    public boolean deleteById(Long id) {
        return storage.remove(id) != null;
    }
}