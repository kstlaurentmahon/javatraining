package com.kstlaurent.springtraining.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import com.kstlaurent.springtraining.model.dto.TaskDTO;
import com.kstlaurent.springtraining.model.entity.Task;
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
    public Task getTaskById(@PathVariable Long id) {
        return taskService.findById(id);
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody Task task) {
        return taskService.save(task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteById(id);
    }
}