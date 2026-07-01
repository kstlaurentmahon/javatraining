package com.kstlaurent.springtraining.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kstlaurent.springtraining.exception.ResourceNotFoundException;
import com.kstlaurent.springtraining.model.entity.Task;
import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.repository.TaskRepository;
import com.kstlaurent.springtraining.model.dto.TaskDTO;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    //added for week 4 entity to dto mapping; helper method
    private TaskDTO mapToDTO(Task task) {
        Long ownerId = null;

        if (task.getOwner()!=null){
            ownerId = task.getOwner().getId();
        }

    return new TaskDTO(
        task.getId(),
        task.getTitle(),
        task.getDescription(),
        ownerId
    );
}

    public TaskService(TaskRepository repository) {
        this.taskRepository = repository;
    }

public List<TaskDTO> findAllDTOs() {

    return taskRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .toList();
}

    public Task findById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }
}