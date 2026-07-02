package com.kstlaurent.springtraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kstlaurent.springtraining.model.entity.Task;

public interface TaskRepository
                extends JpaRepository<Task, Long> {
}
