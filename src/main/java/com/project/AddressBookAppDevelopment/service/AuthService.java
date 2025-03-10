package com.project.AddressBookAppDevelopment.service;

import com.project.AddressBookAppDevelopment.dto.UserDTO;
import com.project.AddressBookAppDevelopment.model.User;
import com.project.AddressBookAppDevelopment.repository.UserRepository;
import com.project.AddressBookAppDevelopment.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserDTO registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new UserDTO(savedUser.getUsername(), savedUser.getEmail());
    }

    public String loginUser(String email, String password) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && passwordEncoder.matches(password, user.get().getPassword())) {
            return jwtUtils.generateToken(email);
        }
        return null;
    }
}
