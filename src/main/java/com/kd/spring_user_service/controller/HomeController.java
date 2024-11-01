package com.kd.spring_user_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/home")
    public String SayHello(){
        return "Hello Spring Boot";
    }
}
