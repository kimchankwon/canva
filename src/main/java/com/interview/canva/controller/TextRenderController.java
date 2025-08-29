package com.interview.canva.controller;

import com.interview.canva.TextRenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TextRenderController {

    @Autowired
    private TextRenderService textRenderService;

    @PostMapping(value = "/render", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> renderText(@RequestBody RenderRequest request) {
        // Validate request
        if (request == null || request.getFontUrl() == null || request.getText() == null) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Error: fontUrl and text are required".getBytes());
        }
        
        if (request.getFontUrl().trim().isEmpty() || request.getText().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body("Error: fontUrl and text cannot be empty".getBytes());
        }
        
        try {
            byte[] imageBytes = textRenderService.renderText(request.getFontUrl(), request.getText());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (Exception e) {
            // Return error response with appropriate status
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Error: " + e.getMessage()).getBytes());
        }
    }

    // Request DTO for the render endpoint
    public static class RenderRequest {
        private String fontUrl;
        private String text;

        // Default constructor
        public RenderRequest() {}

        // Constructor with parameters
        public RenderRequest(String fontUrl, String text) {
            this.fontUrl = fontUrl;
            this.text = text;
        }

        // Getters and setters
        public String getFontUrl() {
            return fontUrl;
        }

        public void setFontUrl(String fontUrl) {
            this.fontUrl = fontUrl;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
