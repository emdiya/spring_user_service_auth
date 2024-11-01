package com.kd.spring_user_service.service;
import com.kd.spring_user_service.common.validation.HandleValidationRequest;
import com.kd.spring_user_service.dto.RoleDto;
import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.*;
import com.kd.spring_user_service.repository.OtpRepository;
import com.kd.spring_user_service.repository.RoleRepository;
import com.kd.spring_user_service.repository.UserRepository;
import com.kd.spring_user_service.common.validation.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailValidator emailValidator;
    private final HandleValidationRequest handleValidationRequest;
    private final OtpService otpService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
    public UserService(UserRepository userRepository, RoleRepository roleRepository, OtpRepository otpRepository, AuthenticationManager authenticationManager, JWTService jwtService, EmailValidator emailValidator, HandleValidationRequest handleValidationRequest, OtpService otpService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.otpRepository = otpRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailValidator = emailValidator;
        this.handleValidationRequest = handleValidationRequest;
        this.otpService = otpService;
    }

    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    public boolean isUsernameExists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    //*Register user
    public ResponseEntity<?>  registerUser(UserModel regUser){

        // Validate register request fields
        ResponseEntity<?> validationResponse = handleValidationRequest.validateRegisterRequest(regUser);
        if (validationResponse != null) {
            return validationResponse;
        }

        boolean isUsernameExists = isUsernameExists(regUser.getUsername());
        boolean isEmailExists = isEmailExists(regUser.getEmail());
        boolean isValidEmail = emailValidator.validateEmail(regUser);

        if(!isValidEmail){
            return new ResponseEntity<>(Response.badRequest("Invalid email"),HttpStatus.BAD_REQUEST);
        }
        if (isUsernameExists) {
            return new ResponseEntity<>(Response.badRequest("username already exists"),HttpStatus.BAD_REQUEST);
        }
        if (isEmailExists) {
            return new ResponseEntity<>(Response.badRequest("email already exists"),HttpStatus.BAD_REQUEST);
        }

        // Fetch role and encode password
        Role userRole = roleRepository.findById(1).orElseThrow(() -> new RuntimeException("Role not found"));
        regUser.setUserRole(userRole);
        String encodedPassword = passwordEncoder.encode(regUser.getPassword());
        regUser.setPassword(encodedPassword);

        // Save user
        UserModel savedUser = userRepository.save(regUser);
        RoleDto roleDto = new RoleDto(savedUser.getUserRole().getId(),savedUser.getUserRole().getRoleName());

        // Convert saved UserModel to UserDto for the response
        UserDto responseUserDto = new UserDto(
                savedUser.getFirstname(),
                savedUser.getLastname(),
                savedUser.getPhoneNumber(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getGender(),
                savedUser.getPosition(),
                savedUser.getDateOfBirth(),
                savedUser.getCity(),
                roleDto
        );
        return new ResponseEntity<>(Response.success("Register successfully",responseUserDto), HttpStatus.OK);
    }



    //? Login
    public ResponseEntity<?> login(UserModel logUser) {
        // Validate login request fields
        ResponseEntity<?> validationResponse = handleValidationRequest.validateLoginRequest(logUser);
        if (validationResponse != null) {
            return validationResponse;
        }

        // Find the user in the repository
        Optional<UserModel> optionalUser = userRepository.findByUsername(logUser.getUsername());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(Response.notFund("User Not Found"), HttpStatus.NOT_FOUND);
        }

        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logUser.getUsername(), logUser.getPassword())
            );

            // If authentication was successful, generate and return the token
            if (authentication.isAuthenticated()) {
                CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(customUserDetail);
                return new ResponseEntity<>(Response.success("Login Successful", Map.of("access_token", token)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Response.failure("Authentication failed"), HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (BadCredentialsException e) {
            // Handle incorrect password case
            return new ResponseEntity<>(Response.failure("Incorrect password"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // Handle other authentication errors
            return new ResponseEntity<>(Response.failure("An error occurred during authentication: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //*forgot password
    public ResponseEntity<?> handleForgotPassword(UserModel forgotPassword) {
        boolean isEmailExists = isEmailExists(forgotPassword.getEmail());
        boolean isValidEmail = emailValidator.validateEmail(forgotPassword);
        if(!isValidEmail){
            return new ResponseEntity<>(Response.badRequest("Invalid email"),HttpStatus.BAD_REQUEST);
        }
        if (!isEmailExists) {
            return new ResponseEntity<>(Response.badRequest("Email not registered"), HttpStatus.BAD_REQUEST);
        }

        otpService.generateAndSendOtp(forgotPassword.getEmail());
        return new ResponseEntity<>(Response.success("OTP has been sent to your email"), HttpStatus.OK);
    }

    //*verifyOtp
    public ResponseEntity<?> verifyOtp(UserModel verifyOtp) {
        // Validate verifyOtp request fields
        ResponseEntity<?> validationResponse = handleValidationRequest.validatVerifyOtpRequest(verifyOtp);
        if (validationResponse != null) {
            return validationResponse;
        }
        boolean isOtpValid = otpService.verifyOtp(verifyOtp.getEmail(), verifyOtp.getOtp());
        boolean isValidEmail = emailValidator.validateEmail(verifyOtp);
        if(!isValidEmail){
            return new ResponseEntity<>(Response.badRequest("Invalid email"),HttpStatus.BAD_REQUEST);
        }

        if (!isOtpValid) {
            return new ResponseEntity<>(Response.badRequest("Invalid OTP!"), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(Response.success("OTP verified successfully"), HttpStatus.OK);
    }

    //*resetPassword
    public ResponseEntity<?> resetPassword(UserModel resetPassword) {
        // Validate verifyOtp request fields
        ResponseEntity<?> validationResponse = handleValidationRequest.validatResetPassword(resetPassword);
        if (validationResponse != null) {
            return validationResponse;
        }
        // Check if the email exists in the system
        Optional<UserModel> userOptional = userRepository.findByEmail(resetPassword.getEmail());
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(Response.badRequest("Email not registered"), HttpStatus.BAD_REQUEST);
        }
        // Check if the OTP has been verified
        Optional<OtpModel> otpRecord = otpRepository.findLatestOtpByEmail(resetPassword.getEmail());
        if (otpRecord.isEmpty() || !otpRecord.get().isVerified()) {
            return new ResponseEntity<>(Response.badRequest("OTP not verified"), HttpStatus.FORBIDDEN);
        }
        // Update the password for the existing user
        UserModel user = userOptional.get();
        System.out.println(resetPassword.getPassword());
        user.setPassword(passwordEncoder.encode(resetPassword.getPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(Response.success("Password reset successfully"), HttpStatus.OK);
    }


    //*update passsword
    public ResponseEntity<?> updatePassword(PasswordModelDTO request, String token) {

        ResponseEntity<?> validationResponse = handleValidationRequest.validatePasswordUpdateRequest(request);
        if (validationResponse != null) {
            return validationResponse;
        }

        String username;
        try {
            username = jwtService.extractUsername(token.replace("Bearer ", ""));
        } catch (Exception e) {
            return new ResponseEntity<>(Response.failure("Invalid token"), HttpStatus.UNAUTHORIZED);
        }

        // Find the user by email
        Optional<UserModel> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(Response.badRequest("User not found"), HttpStatus.BAD_REQUEST);
        }

        // Update the password for the user
        UserModel user = userOptional.get();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(Response.success("Password updated successfully"), HttpStatus.OK);
    }

    //*user info
    public ResponseEntity<?> getUserInfo(String token) {
        // Extract email from the token
        String username;
        try {
            username = jwtService.extractUsername(token.replace("Bearer ", ""));
        } catch (Exception e) {
            return new ResponseEntity<>(Response.failure("Invalid token"), HttpStatus.UNAUTHORIZED);
        }

        // Find user by email
        Optional<UserModel> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(Response.notFund("User not found"), HttpStatus.NOT_FOUND);
        }

        // Prepare and return user data
        UserModel user = userOptional.get();
        RoleDto roleDto = new RoleDto(user.getUserRole().getId(),user.getUserRole().getRoleName());
        // Convert saved UserModel to UserDto for the response
        UserDto responseUserDto = new UserDto(
                user.getFirstname(),
                user.getLastname(),
                user.getPhoneNumber(),
                user.getUsername(),
                user.getEmail(),
                user.getGender(),
                user.getPosition(),
                user.getDateOfBirth(),
                user.getCity(),
                roleDto
        );

        return new ResponseEntity<>(Response.success("User info retrieved successfully", responseUserDto), HttpStatus.OK);
    }



}
