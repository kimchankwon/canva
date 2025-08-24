package com.interview.canva.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.canva.dto.CreateUserRequest;
import com.interview.canva.dto.GetUserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateAndRetrieveUser() throws Exception {
        // Create a user request
        CreateUserRequest request = new CreateUserRequest("John", "Doe", "john.doe@example.com");
        String userJson = objectMapper.writeValueAsString(request);

        // Test POST /api/users
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        // Test GET /api/users
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"));
    }

    @Test
    public void testCreateUserWithInvalidEmail() throws Exception {
        // Create a user request with invalid email
        CreateUserRequest request = new CreateUserRequest("Jane", "Smith", "invalid-email");
        String userJson = objectMapper.writeValueAsString(request);

        // Test POST /api/users with invalid email
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isBadRequest());
    }
}
