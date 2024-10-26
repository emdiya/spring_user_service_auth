package com.kd.spring_user_service.controller;


import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Response> register(@RequestBody UserModel user){
        userService.registerUser(user);
        return new ResponseEntity<>(Response.success(), HttpStatus.OK);
    }
}
