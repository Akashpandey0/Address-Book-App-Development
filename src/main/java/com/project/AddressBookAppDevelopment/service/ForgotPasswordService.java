package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.ResetPasswordDTO;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class ForgotPasswordService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public void generateResetToken(String email) {
        // Use the repository method directly without wrapping
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User with email " + email + " not found."));

        // Generate token and set expiration time
        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setTokenExpiry(LocalDateTime.now().plusMinutes(30));
        userRepository.save(user);

        // Send email with reset link
        String resetLink = "http://localhost:8080/auth/reset?token=" + token;
        emailService.sendEmail(user.getEmail(), "Password Reset Request",
                "Click the link to reset your password: " + resetLink);
    }


    public void resetPassword(String token, ResetPasswordDTO resetPasswordDTO) {
        // Use findByResetToken which already returns Optional
        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token."));

        // Check if the token is expired
        if (user.getTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired. Please request a new reset link.");
        }

        // Reset the password
        user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));
        user.setResetToken(null);
        user.setTokenExpiry(null);
        userRepository.save(user);

        System.out.println("Password reset successful!");
    }
}