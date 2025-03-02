
package com.orleansgo.utilisateur.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.orleansgo.utilisateur.dto.UserRegistrationRequest;
import com.orleansgo.utilisateur.model.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registerUser_shouldReturnUser_whenValidInput() throws Exception {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setEmail("test@orleansgo.com");
        request.setPassword("password123");
        request.setFirstName("Test");
        request.setLastName("User");
        request.setPhoneNumber("+33612345678");
        request.setRole(UserRole.PASSENGER);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@orleansgo.com"))
                .andExpect(jsonPath("$.firstName").value("Test"))
                .andExpect(jsonPath("$.lastName").value("User"));
    }
}
