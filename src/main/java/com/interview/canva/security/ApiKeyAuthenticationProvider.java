package com.interview.canva.security;

import com.interview.canva.entity.ApiKey;
import com.interview.canva.repository.ApiKeyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ApiKeyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String apiKey = authentication.getName();
        
        if (apiKey == null || apiKey.trim().isEmpty()) {
            throw new BadCredentialsException("API key is required");
        }

        // Find the API key in the database
        ApiKey validApiKey = apiKeyRepository.findByKeyValueAndIsActiveTrue(apiKey)
                .orElseThrow(() -> new BadCredentialsException("Invalid API key"));

        // Create authorities from permissions
        List<SimpleGrantedAuthority> authorities = Arrays.stream(validApiKey.getPermissions().split(","))
                .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission))
                .collect(Collectors.toList());

        // Create authenticated token
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                validApiKey.getClientName(),
                null,
                authorities
        );

        // Set additional details
        authToken.setDetails(validApiKey);

        return authToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
