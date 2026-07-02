package com.kstlaurent.springtraining.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;

//week 4 requirement - custom DTOs handling clean JSON payloads without leaking raw entities
//DTOs (Data Transfer Object) - translation layer between database and API
//Think: "what does the user get to see vs what we actually store?"

public class UserAccountDTO {

    private Long id;

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be a valid address")
    private String email;

    public UserAccountDTO() {
    }

    public UserAccountDTO(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // getters
    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    // setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
