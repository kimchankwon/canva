package com.interview.canva.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "api_keys")
@Data
@NoArgsConstructor
@Getter
@Setter
public class ApiKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "key_value", nullable = false, unique = true)
    private String keyValue;

    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Column(name = "permissions", nullable = false)
    private String permissions; // Comma-separated permissions like "READ,WRITE,DELETE"

    @Column(name = "is_active", nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_used")
    private LocalDateTime lastUsed;

    // Constructor for creating new API keys
    public ApiKey(String keyValue, String clientName, String permissions) {
        this.keyValue = keyValue;
        this.clientName = clientName;
        this.permissions = permissions;
        this.isActive = true;
        this.createdAt = LocalDateTime.now();
    }
}
