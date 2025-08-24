package com.interview.canva.config;

import com.interview.canva.entity.ApiKey;
import com.interview.canva.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    public void run(String... args) throws Exception {
        // Only create sample data if no API keys exist
        if (apiKeyRepository.count() == 0) {
            createSampleApiKeys();
        }
    }

    private void createSampleApiKeys() {
        // Full access API key
        ApiKey fullAccessKey = new ApiKey(
            "sample-full-access-key-12345",
            "Full Access Client",
            "READ,WRITE,DELETE"
        );
        apiKeyRepository.save(fullAccessKey);

        // Read-only API key
        ApiKey readOnlyKey = new ApiKey(
            "sample-read-only-key-67890",
            "Read Only Client",
            "READ"
        );
        apiKeyRepository.save(readOnlyKey);

        // Write access API key
        ApiKey writeAccessKey = new ApiKey(
            "sample-write-access-key-11111",
            "Write Access Client",
            "READ,WRITE"
        );
        apiKeyRepository.save(writeAccessKey);

        System.out.println("Sample API keys created:");
        System.out.println("Full Access: sample-full-access-key-12345");
        System.out.println("Read Only: sample-read-only-key-67890");
        System.out.println("Write Access: sample-write-access-key-11111");
    }
}
