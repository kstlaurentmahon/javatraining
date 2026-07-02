package com.kstlaurent.springtraining.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kstlaurent.springtraining.model.entity.UserAccount;

public interface UserAccountRepository
                extends JpaRepository<UserAccount, Long> {

}