package com.interview.canva.controller;

import com.interview.canva.TextRenderService;
import com.interview.canva.dto.RenderRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TextRenderController.class)
public class TextRenderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TextRenderService textRenderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRenderTextSuccess() throws Exception {
        // Mock the service response
        byte[] mockImageBytes = {(byte) 0x89, (byte) 0x50, (byte) 0x4E, (byte) 0x47, (byte) 0x0D, (byte) 0x0A, (byte) 0x1A, (byte) 0x0A}; // PNG header
        when(textRenderService.renderText(anyString(), anyString())).thenReturn(mockImageBytes);

        // Create request body
        RenderRequest request = new RenderRequest(
            "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf",
            "Hello World"
        );

        // Perform POST request
        mockMvc.perform(post("/api/render")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_PNG_VALUE))
                .andExpect(content().bytes(mockImageBytes));
    }

    @Test
    public void testRenderTextWithEmptyBody() throws Exception {
        mockMvc.perform(post("/api/render")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRenderTextWithEmptyText() throws Exception {
        RenderRequest request = new RenderRequest(
            "https://font-public.canva.com/YAFdJkVWBPo/0/MoreSugar-Regular.62992e429acdec5e01c3db.6f7a950ef2bb9f1314d37ac4a660925e.otf",
            ""
        );

        mockMvc.perform(post("/api/render")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRenderTextWithInvalidFontUrl() throws Exception {
        // Mock the service to throw a FontDownloadException
        when(textRenderService.renderText(anyString(), anyString()))
            .thenThrow(new com.interview.canva.FontDownloadException("Invalid font URL"));

        RenderRequest request = new RenderRequest(
            "https://invalid-url.com/font.otf",
            "Hello World"
        );

        mockMvc.perform(post("/api/render")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.TEXT_PLAIN_VALUE));
    }
}
