package com.kd.spring_user_service.service;

import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.repository.AuthRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public AuthService(AuthRepository authRepository, AuthenticationManager authenticationManager, JWTService jwtService) {
        this.authRepository = authRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }


    public void registerUser(UserModel regUser){
        regUser.setPassword(passwordEncoder.encode(regUser.getPassword()));
        authRepository.save(regUser);
    }

}
