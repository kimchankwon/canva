package com.interview.canva.controller;

import com.interview.canva.TextRenderService;
import com.interview.canva.dto.RenderRequest;
import com.interview.canva.FontDownloadException;
import com.interview.canva.FontLoadException;
import com.interview.canva.TextRenderException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class TextRenderController {

    @Autowired
    private TextRenderService textRenderService;

    @PostMapping(value = "/render", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> renderText(@Valid @RequestBody RenderRequest request) {
        try {
            byte[] imageBytes = textRenderService.renderText(request.fontUrl(), request.text());
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(imageBytes);
        } catch (FontDownloadException | FontLoadException | TextRenderException e) {
            // Return specific exception details
            return ResponseEntity.badRequest()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Error: " + e.getMessage()).getBytes());
        } catch (Exception e) {
            // Handle unexpected exceptions
            return ResponseEntity.internalServerError()
                    .contentType(MediaType.TEXT_PLAIN)
                    .body(("Unexpected error: " + e.getMessage()).getBytes());
        }
    }
}
