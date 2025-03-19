package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.model.PasswordResetToken;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.repository.PasswordResetTokenRepository;
import com.project.AddressBookAppDevelopment.repository.UserRepository;
import com.project.AddressBookAppDevelopment.security.JwtUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ✅ REGISTER USER (With Event Publishing)
    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        // Publish event to RabbitMQ
        rabbitTemplate.convertAndSend("user.queue", "New user registered: " + user.getEmail());
    }

    // ✅ LOGIN USER (Uses Redis Cache for Performance)
    @Cacheable(value = "userTokens", key = "#email")
    public String loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtils.generateToken(email);
        }
        return null;
    }

    // ✅ GENERATE PASSWORD RESET TOKEN
    public void generateResetToken(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            String token = UUID.randomUUID().toString();
            Date expiryDate = new Date(System.currentTimeMillis() + 15 * 60 * 1000); // Token valid for 15 minutes

            PasswordResetToken resetToken = new PasswordResetToken(email, token, expiryDate);
            tokenRepository.save(resetToken);

            // Publish event to RabbitMQ
            rabbitTemplate.convertAndSend("password.queue", "Password reset token generated for: " + email);
        }
    }

    // ✅ RESET PASSWORD USING TOKEN
    public boolean resetPassword(String token, String newPassword) {
        Optional<PasswordResetToken> resetToken = tokenRepository.findByToken(token);
        if (resetToken.isPresent() && resetToken.get().getExpiryDate().after(new Date())) {
            Optional<User> user = userRepository.findByEmail(resetToken.get().getEmail());
            if (user.isPresent()) {
                User existingUser = user.get();
                existingUser.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(existingUser);
                tokenRepository.delete(resetToken.get());

                // Publish event to RabbitMQ
                rabbitTemplate.convertAndSend("password.queue", "Password reset successfully for: " + existingUser.getEmail());

                return true;
            }
        }
        return false;
    }
}
