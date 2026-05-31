package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import com.mipt.aleksandrivanovich.second_sem.hw_1.dto.AttachmentResponseDto;
import com.mipt.aleksandrivanovich.second_sem.hw_1.service.AttachmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AttachmentController.class)
class AttachmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AttachmentService attachmentService;

    @Test
    void uploadAttachment_Success() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
            "file",
            "test.txt",
            MediaType.TEXT_PLAIN_VALUE,
            "test content".getBytes()
        );

        AttachmentResponseDto response = new AttachmentResponseDto(
            1L, "test.txt", "text/plain", 12L, LocalDateTime.now()
        );

        when(attachmentService.storeAttachment(anyString(), any()))
            .thenReturn(response);

        mockMvc.perform(multipart("/api/tasks/task-1/attachments")
                .file(file))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.fileName").value("test.txt"));
    }

    @Test
    void downloadAttachment_Success() throws Exception {
        when(attachmentService.loadAsResource(1L))
            .thenReturn(new org.springframework.core.io.ByteArrayResource("test".getBytes()));

        when(attachmentService.getAttachment(1L))
            .thenReturn(new com.mipt.aleksandrivanovich.second_sem.hw_1.model.TaskAttachment());

        mockMvc.perform(get("/api/attachments/1"))
            .andExpect(status().isOk());
    }

    @Test
    void deleteAttachment_Success() throws Exception {
        when(attachmentService.deleteAttachment(1L))
            .thenReturn(true);

        mockMvc.perform(delete("/api/attachments/1"))
            .andExpect(status().isNoContent());
    }

    @Test
    void getTaskAttachments_Success() throws Exception {
        when(attachmentService.getAttachmentsByTaskId("task-1"))
            .thenReturn(java.util.Collections.emptyList());

        mockMvc.perform(get("/api/tasks/task-1/attachments"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray());
    }
}