package com.kd.spring_user_service.model;

import lombok.Data;

@Data
public class PasswordModelDTO {
    private String email;
    private String otp;
    private String newPassword;
    private String currentPassword;
    private String confirmPassword;
}
