package com.kd.spring_user_service.repository;

import com.kd.spring_user_service.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;



public interface AuthRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);

}
