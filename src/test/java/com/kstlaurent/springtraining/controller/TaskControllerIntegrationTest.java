package com.kstlaurent.springtraining.controller;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.model.entity.Task;
import com.kstlaurent.springtraining.repository.TaskRepository;
import com.kstlaurent.springtraining.repository.UserAccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TaskControllerIntegrationTest {

    @Autowired // instead of @Mock/@InjectMocks like in the service tests; no longer building
               // fake objects
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserAccountRepository userRepository;

    private UserAccount owner;

    @BeforeEach
    void setUp() {
        owner = userRepository.save(new UserAccount("jsmith", "jsmith@example.com"));
    }

    // Test 1: GET /api/tasks/{id} happy path
    @Test
    void getTaskById_whenTaskExists_returnsTaskJson() throws Exception {
        Task saved = taskRepository.save(new Task("Write tests", "Cover the API"));
        saved.setOwner(owner);
        taskRepository.save(saved);

        // perform(get(...)) builds and sends a simulated HTTP GET request through the
        // REAL controller
        // this is the actual integration point
        mockMvc.perform(get("/api/tasks/" + saved.getId()))
                .andExpect(status().isOk()) // checks the HTTP status code = 200
                .andExpect(jsonPath("$.title").value("Write tests"))
                .andExpect(jsonPath("$.ownerId").value(owner.getId()));
    }

    // Test 2: GET /api/tasks/{id} not-found
    @Test
    void getTasksById_whenNonexistantTask_returns404() throws Exception {
        // no need to save anything here - just plug an id in
        mockMvc.perform(get("/api/tasks/9999"))
                .andExpect(status().isNotFound()) // checks for the 404
                .andExpect(jsonPath("$.message").value("Task not found with id 9999"));
    }

    // Test 3: create happy path
    @Test
    void createTask_withValidInput_returns201AndTaskJson() throws Exception {

        // send an actual JSON string as the request boy (like postman, but scripted)
        String requestBody = """
                {
                  "title": "New task",
                  "description": "Created via integration test",
                  "ownerId": %d
                }
                """.formatted(owner.getId());

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("New task"))
                .andExpect(jsonPath("$.ownerId").value(owner.getId()));
    }

    // Test 4: create validation failure - tests the @NotBlank annotation on TaskDTO
    @Test
    void createTask_withBlankTitle_returns400() throws Exception {
        String requestBody = """
                {"title": "",
                "description": "Missing title",
                "ownerId": %d}
                """.formatted(owner.getId());

        mockMvc.perform(post("/api/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.title").exists());
    }

    // Test 5: update happy path
    @Test
    void updateTask_withValidInput_returns200AndUpdatedJson() throws Exception {
        Task existing = taskRepository.save(new Task("Old title", "Old description"));
        existing.setOwner(owner);
        existing = taskRepository.save(existing);

        String requestBody = """
                {"title": "Updated title",
                  "description": "Updated description",
                  "ownerId": %d
                }
                """.formatted(owner.getId());

        mockMvc.perform(put("/api/tasks/" + existing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"));
    }

    // Test 6: DELETE /api/tasks/{id} happy path
    @Test
    void deleteTask_whenTaskExists_returns200AndRemovesFromDb() throws Exception {
        Task existing = taskRepository.save(new Task("Delete me", "Temp"));

        mockMvc.perform(delete("/api/tasks/" + existing.getId()))
                .andExpect(status().isOk());

        // proves the row is actually gone from real DB
        assertFalse(taskRepository.existsById(existing.getId()));
    }

    // Test 7: DELETE /api/tasks/{id} not found
    @Test
    void deleteTask_whenTaskDoesNotExist_returns404() throws Exception {
        mockMvc.perform(delete("/api/tasks/9999"))
                .andExpect(status().isNotFound());
    }

}