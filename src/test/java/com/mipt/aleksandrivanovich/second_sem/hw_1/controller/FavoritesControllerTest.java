package com.mipt.aleksandrivanovich.second_sem.hw_1.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PreferencesController.class)
class PreferencesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getViewPreference_DefaultValue() throws Exception {
        mockMvc.perform(get("/api/preferences/view"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.viewMode").value("detailed"));
    }

    @Test
    void setViewPreference_Success() throws Exception {
        mockMvc.perform(post("/api/preferences/view")
                .param("mode", "compact")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.viewMode").value("compact"))
            .andExpect(jsonPath("$.message").value("Preference saved successfully"));
    }

    @Test
    void setViewPreference_InvalidMode() throws Exception {
        mockMvc.perform(post("/api/preferences/view")
                .param("mode", "invalid")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void deleteViewPreference_Success() throws Exception {
        mockMvc.perform(delete("/api/preferences/view")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}