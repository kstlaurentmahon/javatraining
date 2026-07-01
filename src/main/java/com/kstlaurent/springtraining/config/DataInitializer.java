package com.kstlaurent.springtraining.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.kstlaurent.springtraining.model.entity.Task;
import com.kstlaurent.springtraining.model.entity.UserAccount;
import com.kstlaurent.springtraining.repository.UserAccountRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserAccountRepository repository;

    public DataInitializer(UserAccountRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        if (repository.count()>0) {
            System.out.println("Sample data already present, skipping seed.");
            return;
        }

        UserAccount user =
                new UserAccount("kstlaurentmahon", "kathryn.st.laurent-mahon@amivero.com");

        Task task =
                new Task("Week 3: Persistance in Spring",
                         "Verify H2 database");

        task.setOwner(user);
        user.getTasks().add(task);

        repository.save(user);

        System.out.println("Sample data inserted.");
    }
}