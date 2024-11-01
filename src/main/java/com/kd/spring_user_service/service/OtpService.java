package com.kd.spring_user_service.service;

import com.kd.spring_user_service.model.OtpModel;
import com.kd.spring_user_service.repository.OtpRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OtpService {
    private final OtpRepository otpRepository;
    private final MailService mailService;

    public OtpService(OtpRepository otpRepository, MailService mailService) {
        this.otpRepository = otpRepository;
        this.mailService = mailService;
    }

    public String generateOtp() {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    public String generateAndSendOtp(String email) {
        String otp = generateOtp();
        mailService.sendOtpEmail(email, otp);
        OtpModel otpEntity = new OtpModel();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntity);
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        Optional<OtpModel> storedOtpOpt = otpRepository.findByEmailAndOtp(email, otp);
        if (storedOtpOpt.isPresent()) {
            OtpModel storedOtp = storedOtpOpt.get();
            boolean isValid = storedOtp.getOtp().equals(otp) && LocalDateTime.now().isBefore(storedOtp.getExpiresAt());
            if (isValid) {
                storedOtp.setVerified(true);
                otpRepository.save(storedOtp);
            }
            return isValid;
        }
        return false;
    }


}