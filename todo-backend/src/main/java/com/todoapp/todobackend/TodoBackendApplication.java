package com.todoapp.todobackend;

import com.todoapp.todobackend.model.User;
import com.todoapp.todobackend.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder; // Retained for clarity, though registerUser handles encoding

@SpringBootApplication
public class TodoBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(TodoBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(UserService userService, PasswordEncoder passwordEncoder) {
        return args -> {
            // Check if admin user already exists
            try {
                userService.loadUserByUsername("admin");
                System.out.println("Admin user already exists.");
            } catch (org.springframework.security.core.userdetails.UsernameNotFoundException e) {
                // If admin does not exist, create it
                User adminUser = new User();
                adminUser.setUsername("admin");
                // The password will be encoded by the userService.registerUser method
                adminUser.setPassword("admin"); 
                try {
                    userService.registerUser(adminUser);
                    System.out.println("Admin user created successfully.");
                } catch (RuntimeException ex) {
                    // Log the error or handle it as appropriate for your application
                    System.err.println("Error creating admin user: " + ex.getMessage());
                }
            }
        };
    }
}
