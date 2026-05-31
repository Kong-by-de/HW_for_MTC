package com.mipt.aleksandrivanovich.second_sem.hw_1.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * DTO для ответа с информацией о вложении.
 */
@Schema(description = "Ответ с информацией о вложении файла")
public class AttachmentResponseDto {

    @Schema(description = "Идентификатор вложения", example = "1")
    private Long id;

    @Schema(description = "Оригинальное имя файла", example = "document.pdf")
    private String fileName;

    @Schema(description = "Тип содержимого", example = "application/pdf")
    private String contentType;

    @Schema(description = "Размер файла в байтах", example = "102400")
    private long size;

    @Schema(description = "Время загрузки", example = "2025-01-15T10:30:00")
    private LocalDateTime uploadedAt;

    public AttachmentResponseDto() {
    }

    public AttachmentResponseDto(Long id, String fileName, String contentType,
                                 long size, LocalDateTime uploadedAt) {
        this.id = id;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
        this.uploadedAt = uploadedAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public long getSize() { return size; }
    public void setSize(long size) { this.size = size; }

    public LocalDateTime getUploadedAt() { return uploadedAt; }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt = uploadedAt; }
}