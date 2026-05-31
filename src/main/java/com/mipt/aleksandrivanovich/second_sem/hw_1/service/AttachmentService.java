package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.AttachmentResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.TaskAttachment;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskAttachmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Сервис для работы с вложениями задач.
 */
@Service
public class AttachmentService {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    private final TaskAttachmentRepository attachmentRepository;
    private final Path uploadDir;

    public AttachmentService(TaskAttachmentRepository attachmentRepository) {
        this.attachmentRepository = attachmentRepository;
        this.uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    /**
     * Сохраняет файл для задачи.
     */
    public AttachmentResponseDto storeAttachment(String taskId, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String storedFileName = UUID.randomUUID().toString() + extension;

        Path targetPath = uploadDir.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        TaskAttachment attachment = new TaskAttachment();
        attachment.setTaskId(taskId);
        attachment.setFileName(originalFileName);
        attachment.setStoredFileName(storedFileName);
        attachment.setContentType(file.getContentType());
        attachment.setSize(file.getSize());
        attachment.setUploadedAt(LocalDateTime.now());

        attachmentRepository.save(attachment);

        return new AttachmentResponseDto(
            attachment.getId(),
            attachment.getFileName(),
            attachment.getContentType(),
            attachment.getSize(),
            attachment.getUploadedAt()
        );
    }

    /**
     * Получает вложение по ID.
     */
    public TaskAttachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));
    }

    /**
     * Загружает файл как Resource для скачивания.
     */
    public Resource loadAsResource(Long attachmentId) {
        TaskAttachment attachment = getAttachment(attachmentId);

        try {
            Path filePath = uploadDir.resolve(attachment.getStoredFileName());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new RuntimeException("Could not read file: " + attachment.getStoredFileName());
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("Could not load file: " + attachment.getStoredFileName(), e);
        }
    }

    /**
     * Удаляет вложение.
     */
    public boolean deleteAttachment(Long attachmentId) {
        TaskAttachment attachment = attachmentRepository.findById(attachmentId)
            .orElse(null);

        if (attachment == null) {
            return false;
        }

        try {
            Path filePath = uploadDir.resolve(attachment.getStoredFileName());
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            logger.error("Could not delete file: {}", attachment.getStoredFileName(), e);
        }

        // Удаляем запись из репозитория
        return attachmentRepository.deleteById(attachmentId);
    }

    /**
     * Получает все вложения задачи.
     */
    public List<AttachmentResponseDto> getAttachmentsByTaskId(String taskId) {
        return attachmentRepository.findByTaskId(taskId).stream()
            .map(att -> new AttachmentResponseDto(
                att.getId(),
                att.getFileName(),
                att.getContentType(),
                att.getSize(),
                att.getUploadedAt()
            ))
            .collect(Collectors.toList());
    }
}