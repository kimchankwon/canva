package com.interview.canva.service;

import com.interview.canva.dto.CreateUserRequest;
import com.interview.canva.dto.GetUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @Test
    public void testUpdateUserDoesNotChangeEmail() {
        // Create a user
        CreateUserRequest originalRequest = new CreateUserRequest("John", "Doe", "john.doe@example.com");
        GetUserResponse savedUser = userService.createUser(originalRequest);
        assertNotNull(savedUser.id());
        assertEquals("john.doe@example.com", savedUser.email());

        // Try to update with new email
        CreateUserRequest updateRequest = new CreateUserRequest("Jane", "Smith", "jane.smith@example.com");
        GetUserResponse updatedUser = userService.updateUser(savedUser.id(), updateRequest).orElse(null);
        
        assertNotNull(updatedUser);
        assertEquals("Jane", updatedUser.firstName());
        assertEquals("Smith", updatedUser.lastName());
        // Email should remain unchanged
        assertEquals("john.doe@example.com", updatedUser.email());
        assertNotEquals("jane.smith@example.com", updatedUser.email());
    }

    @Test
    public void testCreateAndRetrieveUser() {
        CreateUserRequest request = new CreateUserRequest("Alice", "Johnson", "alice.johnson@example.com");
        GetUserResponse savedUser = userService.createUser(request);
        
        assertNotNull(savedUser.id());
        assertEquals("Alice", savedUser.firstName());
        assertEquals("Johnson", savedUser.lastName());
        assertEquals("alice.johnson@example.com", savedUser.email());
    }
}
