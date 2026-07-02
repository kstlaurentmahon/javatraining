package com.kstlaurent.springtraining.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.repository.UserAccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserAccountControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserAccountRepository userRepository;

    @Test
    void getUserById_whenUserExists_returnsUserJson() throws Exception {
        UserAccount saved = userRepository.save(new UserAccount("jsmith", "jsmith@example.com"));

        mockMvc.perform(get("/api/users/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("jsmith"));
    }

    @Test
    void getUserById_whenUserDoesNotExist_returns404() throws Exception {
        mockMvc.perform(get("/api/users/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_withValidInput_returns201AndUserJson() throws Exception {
        String requestBody = """
                {
                  "username": "newuser",
                  "email": "newuser@example.com"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newuser"));
    }

    @Test
    void createUser_withInvalidEmail_returns400() throws Exception {
        String requestBody = """
                {
                  "username": "newuser",
                  "email": "not-an-email"
                }
                """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void updateUser_withValidInput_returns200AndUpdatedJson() throws Exception {
        UserAccount existing = userRepository.save(new UserAccount("oldname", "old@example.com"));

        String requestBody = """
                {
                  "username": "newname",
                  "email": "new@example.com"
                }
                """;

        mockMvc.perform(put("/api/users/" + existing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("newname"));
    }

    @Test
    void deleteUser_whenUserExists_returns200AndRemovesFromDb() throws Exception {
        UserAccount existing = userRepository.save(new UserAccount("deleteme", "delete@example.com"));

        mockMvc.perform(delete("/api/users/" + existing.getId()))
                .andExpect(status().isOk());

        assertFalse(userRepository.existsById(existing.getId()));
    }

    @Test
    void deleteUser_whenUserDoesNotExist_returns404() throws Exception {
        mockMvc.perform(delete("/api/users/9999"))
                .andExpect(status().isNotFound());
    }
}
