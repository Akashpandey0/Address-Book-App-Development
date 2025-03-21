package com.project.AddressBookAppDevelopment.controller;

import com.project.AddressBookAppDevelopment.dto.LoginDTO;
import com.project.AddressBookAppDevelopment.dto.ResetPasswordDTO;
import com.project.AddressBookAppDevelopment.dto.UserDTO;
import com.project.AddressBookAppDevelopment.service.AuthService;
import com.project.AddressBookAppDevelopment.service.EmailService;
import com.project.AddressBookAppDevelopment.service.ForgotPasswordService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private ForgotPasswordService forgotPasswordService;
    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Registers a new user with provided details.")
    public String register(@RequestBody UserDTO userDTO) {
        authService.registerUser(userDTO);
        emailService.sendWelcomeEmail(userDTO.getEmail(), userDTO.getName());
        return "User Registered Successfully!";
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Login a user")
    public String login(@RequestBody LoginDTO loginDTO) {
        return authService.loginUser(loginDTO.getEmail(), loginDTO.getPassword());
    }

    @PostMapping("/forgot")
    public String forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        forgotPasswordService.generateResetToken(email);
        return "Password reset link sent successfully to your email.";
    }

    @PostMapping("/reset")
    public String resetPassword(@RequestParam String token,@RequestBody @Valid ResetPasswordDTO resetPasswordDTO) {
        forgotPasswordService.resetPassword(token, resetPasswordDTO);
        return "Password reset successfully.";
    }
}