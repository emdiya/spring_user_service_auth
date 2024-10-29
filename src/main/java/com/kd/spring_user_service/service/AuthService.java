package com.kd.spring_user_service.service;

import com.kd.spring_user_service.common.validation.HandleValidationRequest;
import com.kd.spring_user_service.dto.RoleDto;
import com.kd.spring_user_service.dto.UserDto;
import com.kd.spring_user_service.model.CustomUserDetails;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.model.UserModel;
import com.kd.spring_user_service.repository.AuthRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthService {
    private final AuthRepository authRepository;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final HandleValidationRequest handleValidationRequest;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);

    public AuthService(AuthRepository authRepository, AuthenticationManager authenticationManager, JWTService jwtService, HandleValidationRequest handleValidationRequest) {
        this.authRepository = authRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.handleValidationRequest = handleValidationRequest;
    }
    public ResponseEntity<?> login(UserModel logUser) {
        // Validate login request fields
        ResponseEntity<?> validationResponse = handleValidationRequest.validateLoginRequest(logUser);
        if (validationResponse != null) {
            return validationResponse;
        }
        // Find the user in the repository
        Optional<UserModel> optionalUser = authRepository.findByUsername(logUser.getUsername());
        if (optionalUser.isEmpty()) {
            return new ResponseEntity<>(Response.notFund("User NotFound"), HttpStatus.NOT_FOUND);
        }
        UserModel user = optionalUser.get();
        List<Integer> allowedRoles = List.of(2, 3, 4);
        Integer roleId = Math.toIntExact(user.getUserRole().getId());
        if (!allowedRoles.contains(roleId)) {
            return new ResponseEntity<>(Response.forbidden("Users are not allowed"), HttpStatus.FORBIDDEN);
        }
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(logUser.getUsername(), logUser.getPassword())
            );
            if (authentication.isAuthenticated()) {
                CustomUserDetails customUserDetail = (CustomUserDetails) authentication.getPrincipal();
                String token = jwtService.generateToken(customUserDetail);
                return new ResponseEntity<>(Response.success("Login Successfully", Map.of("access_token", token)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Response.failure("Authentication failure"), HttpStatus.UNAUTHORIZED);
            }
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(Response.unauthorized("Invalid username or password"), HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            return new ResponseEntity<>(Response.failure("An error occurred during authentication: " + e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> getUsers(HttpServletRequest request) {

        String token = null;
        Integer roleId = null;
        try {
            // Extract the token from the Authorization header
            String authorizationHeader = request.getHeader("Authorization");
            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                return new ResponseEntity<>(Response.error("Missing or invalid token"), HttpStatus.UNAUTHORIZED);
            }
            // Get the token and extract the role ID
            token = authorizationHeader.substring(7);
             roleId = jwtService.extractRoleId(token);

            // Check if the role ID is 3 or 4
            if (roleId == 2 || roleId == 3 || roleId == 4) {
                List<UserModel> users = authRepository.fetchAllUsers();
                if (users.isEmpty()) {
                    return new ResponseEntity<>(Response.notFund("No users found"), HttpStatus.NOT_FOUND);
                }
                List<UserDto> userDtos = users.stream().map(user -> {
                    RoleDto roleDto = new RoleDto(
                            user.getUserRole().getId(),
                            user.getUserRole().getRoleName()
                    );
                    return new UserDto(
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
                }).collect(Collectors.toList());
                return new ResponseEntity<>(Response.success("User retrieval successful", Map.of("users", userDtos)), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(Response.forbidden("Access denied"), HttpStatus.FORBIDDEN);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(Response.error(e.toString()), HttpStatus.NOT_FOUND);
        }

    }


}
