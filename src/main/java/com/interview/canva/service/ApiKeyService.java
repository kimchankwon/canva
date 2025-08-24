package com.interview.canva.service;

import com.interview.canva.entity.ApiKey;
import com.interview.canva.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int API_KEY_LENGTH = 32;

    public ApiKey createApiKey(String clientName, String permissions) {
        String keyValue = generateApiKey();
        ApiKey apiKey = new ApiKey(keyValue, clientName, permissions);
        return apiKeyRepository.save(apiKey);
    }

    public List<ApiKey> getAllApiKeys() {
        return apiKeyRepository.findAll();
    }

    public Optional<ApiKey> getApiKeyById(Long id) {
        return apiKeyRepository.findById(id);
    }

    public Optional<ApiKey> getApiKeyByValue(String keyValue) {
        return apiKeyRepository.findByKeyValueAndIsActiveTrue(keyValue);
    }

    public ApiKey updateApiKey(Long id, String clientName, String permissions, boolean isActive) {
        return apiKeyRepository.findById(id)
                .map(apiKey -> {
                    apiKey.setClientName(clientName);
                    apiKey.setPermissions(permissions);
                    apiKey.setActive(isActive);
                    return apiKeyRepository.save(apiKey);
                })
                .orElseThrow(() -> new RuntimeException("API key not found"));
    }

    public boolean deleteApiKey(Long id) {
        if (apiKeyRepository.existsById(id)) {
            apiKeyRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public boolean deactivateApiKey(Long id) {
        return apiKeyRepository.findById(id)
                .map(apiKey -> {
                    apiKey.setActive(false);
                    apiKeyRepository.save(apiKey);
                    return true;
                })
                .orElse(false);
    }

    private String generateApiKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(API_KEY_LENGTH);
        
        for (int i = 0; i < API_KEY_LENGTH; i++) {
            sb.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        
        return sb.toString();
    }
}
