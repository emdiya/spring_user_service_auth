package com.kd.spring_user_service.dto;


import com.kd.spring_user_service.model.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Data
public class UserDto {
    private String firstname;
    private String lastname;
    private String phoneNumber;
    private String username;
    private String email;
    private String gender;
    private String position;
    private LocalDateTime dateOfBirth;
    private String city;
    private RoleDto role;

    public UserDto(LocalDateTime dateOfBirth, String firstname, String lastname, String phoneNumber, String username, String email, String gender, String position, String city, RoleDto role) {
        this.dateOfBirth = dateOfBirth;
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.email = email;
        this.gender = gender;
        this.position = position;
        this.city = city;
        this.role = role;
    }
}
