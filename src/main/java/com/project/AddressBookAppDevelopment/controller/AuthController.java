package com.project.AddressBookAppDevelopment.controller;

import com.project.AddressBookAppDevelopment.service.PasswordResetService;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(authService.registerUser(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credentials) {
        String token = authService.loginUser(credentials.get("email"), credentials.get("password"));
        return token != null ? ResponseEntity.ok(Map.of("token", token)) : ResponseEntity.status(401).body("Invalid credentials");
    }

    @Autowired
    private PasswordResetService passwordResetService;

    // 1️⃣ Forgot Password Endpoint - Generate Reset Token and Send Email
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.generateResetToken(email);
        return ResponseEntity.ok("Password reset link has been sent to your email.");
    }

    // 2️⃣ Reset Password Endpoint - Verify Token and Reset Password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

        boolean success = passwordResetService.resetPassword(token, newPassword);
        if (success) {
            return ResponseEntity.ok("Password has been reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired reset token.");
        }
    }
}
