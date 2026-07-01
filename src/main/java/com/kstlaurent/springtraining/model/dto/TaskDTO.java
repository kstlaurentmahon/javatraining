package com.kstlaurent.springtraining.model.dto;

public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private Long ownerId;

    public TaskDTO() {
    }

    public TaskDTO(Long id, String title, String description, Long ownerId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
    }

    //getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Long getOwnerId() {
        return ownerId;
    }

//setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}
