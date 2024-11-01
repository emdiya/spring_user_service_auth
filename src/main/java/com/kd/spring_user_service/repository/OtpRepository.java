package com.kd.spring_user_service.repository;

import com.kd.spring_user_service.model.OtpModel;
import com.kd.spring_user_service.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<OtpModel, Long> {
    Optional<OtpModel> findByEmailAndOtp(String email, String otp);

    @Query(value = "SELECT * FROM otp_model WHERE email = :email ORDER BY created_at DESC LIMIT 1", nativeQuery = true)
    Optional<OtpModel> findLatestOtpByEmail(@Param("email") String email);

}