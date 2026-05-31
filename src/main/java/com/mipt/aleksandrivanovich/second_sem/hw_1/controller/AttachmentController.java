package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.AttachmentResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Контроллер для управления вложениями задач.
 */
@RestController
@RequestMapping("/api")
public class AttachmentController {

    private final AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    /**
     * POST загрузка файла.
     */
    @PostMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<AttachmentResponseDto> uploadAttachment(
        @PathVariable String taskId,
        @RequestParam("file") MultipartFile file) {

        try {
            AttachmentResponseDto attachment = attachmentService.storeAttachment(taskId, file);
            return ResponseEntity.status(HttpStatus.CREATED).body(attachment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET скачивание файла.
     */
    @GetMapping("/attachments/{attachmentId}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable Long attachmentId) {
        Resource resource = attachmentService.loadAsResource(attachmentId);

        String contentType = attachmentService.getAttachment(attachmentId).getContentType();
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
            .contentType(MediaType.parseMediaType(contentType))
            .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + attachmentService.getAttachment(attachmentId).getFileName() + "\"")
            .body(resource);
    }

    /**
     * DELETE удаление вложения.
     */
    @DeleteMapping("/attachments/{attachmentId}")
    public ResponseEntity<Void> deleteAttachment(@PathVariable Long attachmentId) {
        boolean deleted = attachmentService.deleteAttachment(attachmentId);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * GET список вложений задачи.
     */
    @GetMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<List<AttachmentResponseDto>> getTaskAttachments(@PathVariable String taskId) {
        List<AttachmentResponseDto> attachments = attachmentService.getAttachmentsByTaskId(taskId);
        return ResponseEntity.ok(attachments);
    }
}