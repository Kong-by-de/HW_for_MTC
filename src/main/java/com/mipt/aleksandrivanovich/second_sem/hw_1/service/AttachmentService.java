package com.mipt.aleksandrivanovich.second_sem.hw_1.service;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.AttachmentResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.Task;
import com.mipt.aleksandrivanovich.second_sem.hw_1.model.TaskAttachment;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskAttachmentRepository;
import com.mipt.aleksandrivanovich.second_sem.hw_1.repository.TaskRepository;
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

@Service
public class AttachmentService {

    private static final Logger logger = LoggerFactory.getLogger(AttachmentService.class);

    private final TaskAttachmentRepository attachmentRepository;
    private final TaskRepository taskRepository;
    private final Path uploadDir;

    public AttachmentService(TaskAttachmentRepository attachmentRepository, TaskRepository taskRepository) {
        this.attachmentRepository = attachmentRepository;
        this.taskRepository = taskRepository;
        this.uploadDir = Paths.get("uploads").toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Could not create upload directory", e);
        }
    }

    public AttachmentResponseDto storeAttachment(Long taskId, MultipartFile file) throws IOException {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));

        String originalFileName = file.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String storedFileName = UUID.randomUUID().toString() + extension;

        Path targetPath = uploadDir.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);

        TaskAttachment attachment = new TaskAttachment(
            task, originalFileName, storedFileName,
            file.getContentType(), file.getSize()
        );

        attachmentRepository.save(attachment);

        return new AttachmentResponseDto(
            attachment.getId(),
            attachment.getFileName(),
            attachment.getContentType(),
            attachment.getSize(),
            attachment.getUploadedAt()
        );
    }

    public TaskAttachment getAttachment(Long attachmentId) {
        return attachmentRepository.findById(attachmentId)
            .orElseThrow(() -> new RuntimeException("Attachment not found with id: " + attachmentId));
    }

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

        attachmentRepository.deleteById(attachmentId);
        return true;
    }

    public List<AttachmentResponseDto> getAttachmentsByTaskId(Long taskId) {
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