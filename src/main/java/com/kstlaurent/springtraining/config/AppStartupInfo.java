package com.kstlaurent.springtraining.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

//added to satisfy deliverable from week 2:
//Verified your configuration setup by printing successfully initialized bean properties to the console on startup. 

@Component
public class AppStartupInfo implements CommandLineRunner {

    @Value("${app.name}")
    private String appName;

    @Value("${app.maintainer}")
    private String maintainer;

    @Override
    public void run(String... args) {
        System.out.println("Starting up: " + appName + " (maintained by " + maintainer + ")");
    }
}