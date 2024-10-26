package com.kd.spring_user_service.model;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;


@AllArgsConstructor
@Entity
@Data
@Table(name = "users")
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;
    private String lastname;
    private String phoneNumber;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true)
    private String email;

    private String password;
    private String image;
    private String gender;
    private String position;

    private LocalDateTime dateOfBirth;
    private String city;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private Boolean active = true;

    @ManyToOne
    @JoinColumn(name = "role_id",nullable = false)
    private Role userRole;

    public UserModel() {

    }


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    public String getRole() {
        return userRole != null ? userRole.getRoleName() : null;
    }

    // Constructor with required parameters
    public UserModel(String firstname, String lastname, String phoneNumber, String username, String email, String password, String gender, String position, LocalDateTime dateOfBirth, String city, Role userRole) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNumber = phoneNumber;
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.position = position;
        this.dateOfBirth = dateOfBirth;
        this.city = city;
        this.userRole = userRole;
    }

}
