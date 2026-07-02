//Overall goal: hand the service fake task and user repos that return exactly what we tell them to
//so we can test that the service logic behaves as expected

package com.kstlaurent.springtraining.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kstlaurent.springtraining.exception.ResourceNotFoundException;
import com.kstlaurent.springtraining.model.dto.TaskDTO;
import com.kstlaurent.springtraining.model.entity.Task;
import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.repository.TaskRepository;
import com.kstlaurent.springtraining.repository.UserAccountRepository;

//tells JUnit 5 to let Mockito process the mock anf inject mocks annotations
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // mock creates a fake task repo and user repo. returns null by default
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserAccountRepository userRepository;

    // this is key for the test - creates a REAL TaskService instance and
    // hands them the user and task mock repos through the constructor
    @InjectMocks
    private TaskService taskService;

    private UserAccount owner;

    // want this to run before every test method to give us a fresh owner object
    // every time
    // keeps tests from leaking into each other
    @BeforeEach
    void setUp() {
        owner = new UserAccount("jsmith", "jsmith@example.com");
        // normally id comes from the DB on save, but we're not saving for real here,
        // so we set it manually to simulate "this user already exists with id 1"
        owner.setId(1L);
    }

    // Tests for create()
    // Test 1: happy path for create()
    @Test
    void create_withValidOwner_returnsTaskDTOWithOwnerId() {
        // Arrange: tell the mocks what to return when TaskService calls them
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));
        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            t.setId(99L); // simulate the DB assigning an id on insert
            return t;
        });

        TaskDTO input = new TaskDTO(null, "Write tests", "Cover create()", 1L);

        // Act
        TaskDTO result = taskService.create(input);

        // Assert
        assertEquals(99L, result.getId());
        assertEquals("Write tests", result.getTitle());
        assertEquals(1L, result.getOwnerId());
        // note here: this isn't checking a return value, it's checking if the method
        // was actually called
        verify(taskRepository).save(any(Task.class));
    }

    // Test 2: not-found path for create()
    // covers the case were userRepository.findById(...) returns Optional.empty()
    @Test
    void create_withNonexistentOwner_throwResourceNotFoundException() {

        // Arrange: tell the mocks what to return when TaskService calls them
        // need the opposite of test 1 ie owner doesn't exist
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        TaskDTO input = new TaskDTO(null, "Write tests", "Cover create()", 1L);

        // Act + Assert - assertThrows invokes create() AND checks the outcome
        // whenever you're testing for something that throws, Act + Assert collapse into
        // one line
        // because there's no meaningful resut to seperate out and check - the throwing
        // is the result
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.create(input);
        });

        // directly checks service's control flow
        verify(taskRepository, never()).save(any(Task.class));

    }

    // Tests for update()
    // Test 3: happy path for update()
    // Task exist and owner exists
    // Title, description, and owner get updated, mapped, and DTO comes back
    // correctly
    // Note: need an existing task object
    @Test
    void update_withValidTaskAndOwner_returnsTaskDTOWithOwnerId() {
        // Arrange
        when(userRepository.findById(1L)).thenReturn(Optional.of(owner));

        Task existingTask = new Task("Old title", "Old description");
        existingTask.setId(5L);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(existingTask));

        when(taskRepository.save(any(Task.class))).thenAnswer(invocation -> {
            Task t = invocation.getArgument(0);
            return t;
        });

        TaskDTO input = new TaskDTO(null, "Write tests", "Cover create()", 1L);

        // Act
        TaskDTO result = taskService.update(5L, input);

        // Assert
        assertEquals(5L, result.getId());
        assertEquals("Write tests", result.getTitle());
        assertEquals(1L, result.getOwnerId());
        // note here: this isn't checking a return value, it's checking if the method
        // was actually called
        verify(taskRepository).save(any(Task.class));

    }

    // Test 4: task-not-found path for update()
    // Task doesn't exist; taskRepository.findById(id) returns empty
    // SHOULD throw a resource not found exception
    @Test
    void update_withNonexistantTask_throwsResourceNotFound() {
        // oh no our task, it's empty
        // stub the missing thing as empty
        when(taskRepository.findById(5L)).thenReturn(Optional.empty());

        // scenario: PUT request for a task id that no longer exists
        TaskDTO input = new TaskDTO(null, "Write tests", "Cover update()", 1L);

        // expect the throw
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(5L, input);
        });

        // verify something didn't happen
        verify(userRepository, never()).findById(any());

    }

    // Test 5: owner-not-found path for update()
    // task is found but owner doesn't exist
    // SHOULD throw a resource not found exception
    @Test
    void update_withValidTaskAndNonexistantOwner_throwsResourceNotFound() {
        // stub taskRepo find by id to return a real task
        Task existingTask = new Task("Old title", "Old description");
        existingTask.setId(5L);

        when(taskRepository.findById(5L)).thenReturn(Optional.of(existingTask));

        TaskDTO input = new TaskDTO(null, "Write tests", "Cover create()", 1L);

        // stub userRepo find by id to return empty
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.update(5L, input);
        });

        verify(taskRepository, never()).save(any(Task.class));

    }

    // Test for deleteById()
    // Test 6: deletebyId() happy path
    // task exists, method gets invoked, no exception

    @Test
    void deleteById_withValidTask_taskDeleted() {

        // simulate an existing task by stubbing the boolean
        when(taskRepository.existsById(5L)).thenReturn(true);

        taskService.deleteById(5L);

        verify(taskRepository).deleteById(5L);

    }

    // Test 7: task doesn't exist
    // same shape as create()/update() not-found tests

    @Test
    void deleteById_withNonexistantTask_throwsResourceNotFound() {
        // stub existsbyId to return false
        when(taskRepository.existsById(5L)).thenReturn(false);
        // wrap call in assertThrows
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteById(5L);
        });
        // verify method never called
        verify(taskRepository, never()).deleteById(5L);
    }

}
