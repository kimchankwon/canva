package com.interview.canva.controller;

import com.interview.canva.entity.ApiKey;
import com.interview.canva.service.ApiKeyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApiKeyController.class)
public class ApiKeyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ApiKeyService apiKeyService;

    @Test
    public void testCreateApiKey() throws Exception {
        ApiKey mockApiKey = new ApiKey("test-key-123", "Test Client", "READ,WRITE");
        mockApiKey.setId(1L);
        mockApiKey.setCreatedAt(LocalDateTime.now());

        when(apiKeyService.createApiKey("Test Client", "READ,WRITE")).thenReturn(mockApiKey);

        mockMvc.perform(post("/api/keys")
                .param("clientName", "Test Client")
                .param("permissions", "READ,WRITE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.clientName").value("Test Client"))
                .andExpect(jsonPath("$.permissions").value("READ,WRITE"));
    }

    @Test
    public void testGetAllApiKeys() throws Exception {
        ApiKey mockApiKey1 = new ApiKey("key1", "Client1", "READ");
        mockApiKey1.setId(1L);
        ApiKey mockApiKey2 = new ApiKey("key2", "Client2", "WRITE");
        mockApiKey2.setId(2L);

        when(apiKeyService.getAllApiKeys()).thenReturn(Arrays.asList(mockApiKey1, mockApiKey2));

        mockMvc.perform(get("/api/keys"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }
}
