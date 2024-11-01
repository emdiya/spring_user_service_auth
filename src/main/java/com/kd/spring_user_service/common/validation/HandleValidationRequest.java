package com.kd.spring_user_service.common.validation;

import com.kd.spring_user_service.model.PasswordModelDTO;
import com.kd.spring_user_service.model.Response;
import com.kd.spring_user_service.model.UserModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class HandleValidationRequest {

    public ResponseEntity<?> validateRegisterRequest(UserModel logUser) {
        if (logUser.getEmail() == null || logUser.getEmail().isEmpty()) {
            return createBadRequestResponse("email is required!");
        }
        if (logUser.getUsername() == null || logUser.getUsername().isEmpty()) {
            return createBadRequestResponse("username is required!");
        }
        if (logUser.getPassword() == null || logUser.getPassword().isEmpty()) {
            return createBadRequestResponse("password is required!");
        }

        return null;
    }

    public ResponseEntity<?> validatVerifyOtpRequest(UserModel logUser) {
        if (logUser.getEmail() == null || logUser.getEmail().isEmpty()) {
            return createBadRequestResponse("email is required!");
        }

        if (logUser.getOtp() == null || logUser.getOtp().isEmpty()) {
            return createBadRequestResponse("otp is required!");
        }

        return null;
    }

    public ResponseEntity<?> validatResetPassword(UserModel logUser) {
        if (logUser.getEmail() == null || logUser.getEmail().isEmpty()) {
            return createBadRequestResponse("email is required!");
        }

        if (logUser.getPassword() == null || logUser.getPassword().isEmpty()) {
            return createBadRequestResponse("password is required!");
        }

        return null;
    }

    public ResponseEntity<?> validateLoginRequest(UserModel logUser) {
        if (logUser.getUsername() == null || logUser.getUsername().isEmpty()) {
            return createBadRequestResponse("Username is required!");
        }

        if (logUser.getPassword() == null || logUser.getPassword().isEmpty()) {
            return createBadRequestResponse("Password is required!");
        }

        return null;
    }

    private ResponseEntity<?> createBadRequestResponse(String message) {
        return new ResponseEntity<>(Response.badRequest(message), HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity<?> validatePasswordUpdateRequest(PasswordModelDTO request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            return new ResponseEntity<>(Response.badRequest("Passwords do not match"), HttpStatus.BAD_REQUEST);
        }
        return null;
    }



}
