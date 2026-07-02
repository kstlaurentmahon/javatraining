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
import com.kstlaurent.springtraining.model.dto.UserAccountDTO;
import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.repository.UserAccountRepository;

@ExtendWith(MockitoExtension.class)
public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userRepository;

    @InjectMocks
    private UserAccountService userAccountService;

    private UserAccount owner;

    
    @BeforeEach
    void setUp() {
       owner = new UserAccount("jsmith","jsmith@example.com");
    }

    //methods for testing: create(),update(),deleteById()

    //create()
    // Test 1: create() happy path
    //note that create() is fully covered with one test, 
    //since it doesn't have any dependencies like task create has on owner
    @Test
    void create_withValidInput_returnsUserAccountDTO() {
    
        when(userRepository.save(any(UserAccount.class))).thenAnswer(invocation -> {
        UserAccount u = invocation.getArgument(0);
        u.setId(99L); // simulate the DB assigning an id on insert
        return u;
    });
      
    UserAccountDTO input = new UserAccountDTO(null, "jsmith","jsmith@example.com");
  
    // Act
    UserAccountDTO result = userAccountService.create(input);

    //Assert
    assertEquals(99L, result.getId());
    assertEquals("jsmith", result.getUsername());
    assertEquals("jsmith@example.com", result.getEmail());
   
    
    verify(userRepository).save(any(UserAccount.class));
    }

    // Test 2: update() happy path
    @Test
    void update_withValidInput_returnsUserAccountDTO() {
          
        UserAccount existingUserAccount = new UserAccount("jsmith","jsmith@example.com");
        existingUserAccount.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUserAccount));

    when(userRepository.save(any(UserAccount.class))).thenAnswer(invocation -> {
        UserAccount t = invocation.getArgument(0);
        return t;
    });

    UserAccountDTO input = new UserAccountDTO(null, "jdoe","jdoe@example.com");

    // Act
    UserAccountDTO result = userAccountService.update(1L,input);

    // Assert
    assertEquals(1L, result.getId());
    assertEquals("jdoe", result.getUsername());
    assertEquals("jdoe@example.com", result.getEmail());
    //note here: this isn't checking a return value, it's checking if the method was actually called
    verify(userRepository).save(any(UserAccount.class));
     
    }

    // Test 3: update() nonexistant owner
    @Test
    void update_withNonexistantOwner_throwsResourceNotFound(){
        
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        UserAccountDTO input = new UserAccountDTO(null, "jdoe","jdoe@example.com");

        //expect the throw
        assertThrows(ResourceNotFoundException.class,() -> {userAccountService.update(1L,input);});

        //verify something didn't happen
        verify(userRepository, never()).save(any(UserAccount.class));
    }

    // Test 4: deletebyId() happy path
// task exists, method gets invoked, no exception
    @Test
    void deleteById_withValidUser_userDeleted(){

        //simulate an existing task by stubbing the boolean
        when(userRepository.existsById(1L)).thenReturn(true);

        userAccountService.deleteById(1L);

        verify(userRepository).deleteById(1L);


    }

// Test 5: task doesn't exist
//same shape as create()/update() not-found tests
    @Test
    void deleteById_withNonexistantUser_throwsResourceNotFound(){
        //stub existsById to return false
        when(userRepository.existsById(1L)).thenReturn(false);
        //wrap call in assertThrows
        assertThrows(ResourceNotFoundException.class,() -> { userAccountService.deleteById(1L);});
        //verify method never called
        verify(userRepository,never()).deleteById(1L);
    }
}
