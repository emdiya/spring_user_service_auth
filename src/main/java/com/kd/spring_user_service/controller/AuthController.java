package com.kd.spring_user_service.controller;


import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.service.AuthService;
import com.kd.spring_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel user){
        try {
            return  authService.login(user);

        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers() {
        try {
            return  authService.getUsers();

        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.NOT_FOUND);
        }
    }
}
