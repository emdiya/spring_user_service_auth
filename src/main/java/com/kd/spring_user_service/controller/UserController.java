package com.kd.spring_user_service.controller;
import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.PasswordModelDTO;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.service.OtpService;
import com.kd.spring_user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService, OtpService otpService) {
        this.userService = userService;
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserModel user){
        try{
            return  userService.registerUser(user);
        }catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.NOT_FOUND);
        }

    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserModel user) {
        try {
             return userService.login(user);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody UserModel forgotPassword) {
        try {
            return userService.handleForgotPassword(forgotPassword);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/verify_otp")
    public ResponseEntity<?> verifyOtp(@RequestBody UserModel request) {
        try {
            return userService.verifyOtp(request);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @PostMapping("/reset_password")
    public ResponseEntity<?> resetPassword(@RequestBody UserModel request) {
        try {
            return userService.resetPassword(request);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-password")
    public ResponseEntity<?> updatePassword(
            @RequestBody PasswordModelDTO request,
            @RequestHeader("Authorization") String token) {
        try{
            return userService.updatePassword(request, token);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/info")
    public ResponseEntity<?> userInfo(
            @RequestHeader("Authorization") String token) {
        try{
            return userService.getUserInfo(token);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
