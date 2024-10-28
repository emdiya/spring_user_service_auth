package com.kd.spring_user_service;

import com.kd.spring_user_service.model.Role;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.repository.RoleRepository;
import com.kd.spring_user_service.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

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
            String username = "kd.admin";
            String email = "kd.admin@gmail.com";
            String password ="123456";
            String encodedPassword = passwordEncoder.encode(password);
            if (userRepository.findByUsername(username).isEmpty() && userRepository.findByEmail(email).isEmpty()){
                UserModel adminUser = new UserModel(
                        "KD",
                        "Admin",
                        "09090900",
                        username,
                        email,
                        encodedPassword,
                        "Male",
                        "Super Admin",
                        LocalDateTime.now(),
                        "Phnom Penh",
                        new Role(4, "Super Admin")
                );
                userRepository.save(adminUser);
            }
        };
    }
}
