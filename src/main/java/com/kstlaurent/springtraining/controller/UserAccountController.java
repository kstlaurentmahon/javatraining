package com.kstlaurent.springtraining.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kstlaurent.springtraining.model.dto.UserAccountDTO;
import com.kstlaurent.springtraining.service.UserAccountService;

//week 4 - all enpoints now speak to DTOs exclusively, no more leaking raw entities

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    //updated for week 4 entity to dto mapping, first endpoint mapped yay!
    @GetMapping
    public List<UserAccountDTO> getAllUsers() {
        return userAccountService.findAllDTOs();
    }

    @GetMapping("/{id}")
    public UserAccountDTO getUserById(@PathVariable Long id) {
        return userAccountService.findDTOById(id);
    }

    @PostMapping
    public ResponseEntity<UserAccountDTO> createUser(@Valid @RequestBody UserAccountDTO dto) {
        UserAccountDTO created = userAccountService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userAccountService.deleteById(id);
    }

    @PutMapping("/{id}")
    public UserAccountDTO updateUser(
        @PathVariable Long id,
        @Valid @RequestBody UserAccountDTO dto) {

    return userAccountService.update(id, dto);
}
}
