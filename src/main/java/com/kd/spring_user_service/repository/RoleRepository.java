package com.kd.spring_user_service.repository;

import com.kd.spring_user_service.model.Role;
import com.kd.spring_user_service.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findById(long id);
}
