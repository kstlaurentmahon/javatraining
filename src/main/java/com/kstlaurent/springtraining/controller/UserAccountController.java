package com.kstlaurent.springtraining.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.kstlaurent.springtraining.model.dto.UserAccountDTO;
import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.service.UserAccountService;

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
    public UserAccount getUserById(@PathVariable Long id) {
        return userAccountService.findById(id);
    }

    @PostMapping
    public UserAccount createUser(@Valid @RequestBody UserAccount user) {
        return userAccountService.save(user);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userAccountService.deleteById(id);
    }
}
