package com.kstlaurent.springtraining.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.kstlaurent.springtraining.model.UserAccount;
import com.kstlaurent.springtraining.service.UserAccountService;

@RestController
@RequestMapping("/api/users")
public class UserAccountController {

    private final UserAccountService userAccountService;

    public UserAccountController(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @GetMapping
    public List<UserAccount> getAllUsers() {
        return userAccountService.findAll();
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
