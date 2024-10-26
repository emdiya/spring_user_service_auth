package com.kd.spring_user_service;

import com.kd.spring_user_service.model.Role;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.repository.RoleRepository;
import com.kd.spring_user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Bean
    CommandLineRunner initData() {
        return args -> {
            // Initialize roles
            if (roleRepository.findAll().isEmpty()) {
                Role userRole = roleRepository.save(new Role("User"));
                roleRepository.save(new Role("Editor"));
                roleRepository.save(new Role("Admin"));
                roleRepository.save(new Role("Super Admin"));
            }

            // Check if the admin user already exists
            String username = "adminUser";
            String email = "admin@example.com";
            if (userRepository.findByUsername(username).isEmpty() && userRepository.findByEmail(email).isEmpty()) {
                // Initialize a user with the Admin role
                UserModel adminUser = new UserModel(
                        "Admin",
                        "User",
                        "1234567890",
                        username,
                        email,
                        "adminPass",
                        "Male",
                        "Administrator",
                        LocalDateTime.now(),
                        "Admin City",
                        new Role(4, "Super Admin")
                );
                userRepository.save(adminUser);
            }
        };
    }
}
