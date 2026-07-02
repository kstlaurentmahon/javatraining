package com.kstlaurent.springtraining.controller;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.kstlaurent.springtraining.model.dto.TaskDTO;
import com.kstlaurent.springtraining.service.TaskService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
    return taskService.findAllDTOs();
}

    @GetMapping("/{id}")
    public TaskDTO getTaskById(@PathVariable Long id) {
        return taskService.findDTOById(id);
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody TaskDTO dto) {
        TaskDTO created = taskService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }


    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
    }

    @PutMapping("/{id}")
    public TaskDTO updateTask(
        @PathVariable Long id,
        @Valid @RequestBody TaskDTO dto) {

        return taskService.update(id, dto);
    }
}

    