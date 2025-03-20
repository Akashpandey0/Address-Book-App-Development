package com.project.AddressBookAppDevelopment.controller;

import com.project.AddressBookAppDevelopment.service.PasswordResetService;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication APIs")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register User", description = "Registers a new user")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        authService.registerUser(request.get("username"), request.get("email"), request.get("password"));
        return ResponseEntity.ok("User registered successfully");
    }


    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Logs in a user and returns JWT token")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credentials) {
        String token = authService.loginUser(credentials.get("email"), credentials.get("password"));
        return token != null ? ResponseEntity.ok(token) : ResponseEntity.badRequest().body("Invalid credentials");
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
