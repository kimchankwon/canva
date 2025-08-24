package com.interview.canva.controller;

import com.interview.canva.entity.ApiKey;
import com.interview.canva.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/keys")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    @PostMapping
    public ApiKey createApiKey(@RequestParam String clientName, @RequestParam String permissions) {
        return apiKeyService.createApiKey(clientName, permissions);
    }

    @GetMapping
    public List<ApiKey> getAllApiKeys() {
        return apiKeyService.getAllApiKeys();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiKey> getApiKeyById(@PathVariable Long id) {
        return apiKeyService.getApiKeyById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiKey> updateApiKey(
            @PathVariable Long id,
            @RequestParam String clientName,
            @RequestParam String permissions,
            @RequestParam(defaultValue = "true") boolean isActive) {
        try {
            ApiKey updatedApiKey = apiKeyService.updateApiKey(id, clientName, permissions, isActive);
            return ResponseEntity.ok(updatedApiKey);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteApiKey(@PathVariable Long id) {
        if (apiKeyService.deleteApiKey(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<?> deactivateApiKey(@PathVariable Long id) {
        if (apiKeyService.deactivateApiKey(id)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
