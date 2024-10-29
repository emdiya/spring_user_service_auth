package com.kd.spring_user_service.repository;

import com.kd.spring_user_service.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserModel, Long> {
    Optional<UserModel> findByUsername(String username);
    Optional<UserModel> findByEmail(String email);

    @Query("SELECT u FROM UserModel u LEFT JOIN FETCH u.userRole WHERE u.username = :username OR u.email = :email")
    Optional<UserModel> findByUsernameOrEmail(@Param("username") String username, @Param("email") String email);

}
