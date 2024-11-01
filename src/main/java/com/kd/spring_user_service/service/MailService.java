package com.kd.spring_user_service.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Value("${spring.mail.username}")
    private String sender;
    private final JavaMailSender emailSender;

    public MailService(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public void sendOtpEmail(String email, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("KD App <" + sender + ">");
        message.setTo(email);
        message.setSubject("Your OTP Code");
        message.setText("Dear user,\n\nYour OTP code is: " + otp + "\n\nThis code is valid for the next 5 minutes. Please do not share this code with anyone.\n\nThank you!");
        emailSender.send(message);

    }
}