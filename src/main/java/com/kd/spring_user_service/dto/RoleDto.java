package com.kd.spring_user_service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDto {
    private long id;
    private String roleName;
}

