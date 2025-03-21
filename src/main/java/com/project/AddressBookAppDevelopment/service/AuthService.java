package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.UserDTO;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.repository.UserRepository;
import com.project.AddressBookAppDevelopment.util.JWTUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class AuthService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTUtility jwtUtility;

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    StringRedisTemplate redisTemplate;

    public User registerUser(UserDTO userDTO) {
        userDTO.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User user = new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword(), userDTO.getRole());
        return userRepository.save(user);
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found!"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials!");
        }
        String role = user.getRole();
        String token=jwtUtility.generateToken(email,role);
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set("TOKEN_" + email, token, 1, TimeUnit.HOURS);
        return token;
    }
}