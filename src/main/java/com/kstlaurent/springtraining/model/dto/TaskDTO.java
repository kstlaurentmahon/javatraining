package com.kstlaurent.springtraining.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

//note: need to copy the size and not blank annotations, task dto will NOT inherit validation from task. ditto with the imports.

public class TaskDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    @Size(max = 100, message = "Title must be 100 characters or fewer")
    private String title;

    @Size(max = 500, message = "Description must be 500 characters or fewer")
    private String description;

    @NotNull(message = "ownerId is required")
    private Long ownerId;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
    }

    // getters & setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
