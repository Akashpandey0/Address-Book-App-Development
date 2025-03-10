package com.project.AddressBookAppDevelopment.controller;

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
}
