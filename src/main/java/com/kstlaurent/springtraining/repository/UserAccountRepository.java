package com.kstlaurent.springtraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kstlaurent.springtraining.model.UserAccount;

public interface UserAccountRepository
        extends JpaRepository<UserAccount, Long> {

        }