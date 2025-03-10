package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.model.PasswordResetToken;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.repository.PasswordResetTokenRepository;
import com.project.AddressBookAppDevelopment.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // Generate Reset Token and Send Email
    public void generateResetToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            Date expiryDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000); // 15 minutes expiry

            PasswordResetToken resetToken = new PasswordResetToken(email, token, expiryDate);
            tokenRepository.save(resetToken);

            String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + token;
            emailService.sendEmail(email, "Password Reset Request", "Click the link to reset your password: " + resetLink);
        }
    }

    // Verify Token and Reset Password
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        if (resetToken.isPresent() && resetToken.get().getExpiryDate().after(new Date())) {
            Optional<User> user = userRepository.findByEmail(resetToken.get().getEmail());
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(existingUser);
                tokenRepository.delete(resetToken.get());
                return true;
            }
        }
        return false;
    }
}
