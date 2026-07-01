package com.kstlaurent.springtraining.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kstlaurent.springtraining.exception.ResourceNotFoundException;
import com.kstlaurent.springtraining.repository.UserAccountRepository;
import com.kstlaurent.springtraining.model.dto.UserAccountDTO;
import com.kstlaurent.springtraining.model.entity.UserAccount;

@Service
public class UserAccountService {

    private final UserAccountRepository userRepository;

    //added for week 4 entity to dto mapping; helper method
    private UserAccountDTO mapToDTO(UserAccount user) {
    return new UserAccountDTO(
        user.getId(),
        user.getUsername(),
        user.getEmail()
    );
}

    public UserAccountService(UserAccountRepository repository) {
        this.userRepository = repository;
    }

    //updated for week 4 entity to dto mapping
    public List<UserAccountDTO> findAllDTOs() {

        return userRepository.findAll()
            .stream()
            .map(this::mapToDTO)
            .toList();
}

    public UserAccount findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public UserAccount save(UserAccount user) {
        return userRepository.save(user);
    }

    public void deleteById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User not found with id " + id);
        }
        userRepository.deleteById(id);
    }

    

}