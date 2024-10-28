package com.kd.spring_user_service.service;
import com.kd.spring_user_service.common.validation.HandleValidationRequest;
import com.kd.spring_user_service.dto.RoleDto;
import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.CustomUserDetails;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.model.Role;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.repository.RoleRepository;
import com.kd.spring_user_service.repository.UserRepository;
import com.kd.spring_user_service.common.validation.EmailValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final EmailValidator emailValidator;
    private final HandleValidationRequest handleValidationRequest;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public UserService(UserRepository userRepository, RoleRepository roleRepository, AuthenticationManager authenticationManager, JWTService jwtService, EmailValidator emailValidator, HandleValidationRequest handleValidationRequest) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.emailValidator = emailValidator;
        this.handleValidationRequest = handleValidationRequest;
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
            return new ResponseEntity<>(Response.notFund("User NotFound"),HttpStatus.NOT_FOUND);
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
                return new ResponseEntity<>(Response.success("Login Successfully",Map.of("access_token",token)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Response.failure("Authentication failure"),HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Response.failure("An error occurred during authentication: " + e.getMessage()),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





}
