package com.kstlaurent.springtraining.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kstlaurent.springtraining.exception.ResourceNotFoundException;
import com.kstlaurent.springtraining.model.entity.Task;
import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.repository.TaskRepository;
import com.kstlaurent.springtraining.repository.UserAccountRepository;
import com.kstlaurent.springtraining.model.dto.TaskDTO;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserAccountRepository userRepository;

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

    public TaskService(TaskRepository taskRepository,
                       UserAccountRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
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

    public TaskDTO findDTOById(Long id){
        return mapToDTO(findById(id));
    }

    public void deleteById(Long id) {
        if (!taskRepository.existsById(id)) {
            throw new ResourceNotFoundException("Task not found with id " + id);
        }
        taskRepository.deleteById(id);
    }

    public TaskDTO update(Long id, TaskDTO dto) {
        Task task = taskRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Task not found with id " + id));
            
        UserAccount owner = userRepository.findById(dto.getOwnerId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.getOwnerId()));

        task.setTitle(dto.getTitle());
        task.setDescription(dto.getDescription());
        task.setOwner(owner);

        Task saved = taskRepository.save(task);
        return mapToDTO(saved);
    }

    //replacing this with the update method in the controller, but keep it around for internal use
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    //like the update method but instead of fetching an existing task by id, construct a new one
    public TaskDTO create(TaskDTO dto){
        UserAccount owner = userRepository.findById(dto.getOwnerId())
        .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + dto.getOwnerId()));

        Task task = new Task(dto.getTitle(),dto.getDescription());
        task.setOwner(owner);

        Task saved = taskRepository.save(task);
        return mapToDTO(saved);
    }


}