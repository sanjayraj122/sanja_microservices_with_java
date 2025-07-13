package com.Auth_service.Auth.service.impl;

import com.Auth_service.Auth.entity.UserDtails;
import com.Auth_service.Auth.exception.ResourceNotFoundException;
import com.Auth_service.Auth.repository.UserRepository;
import com.Auth_service.Auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDtails saveUser(UserDtails userDetails) {
        userDetails.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        UserDtails savedUser= userRepository.save(userDetails);
        return savedUser;
    }

    @Override
    public UserDtails getUserByEmail(String username) {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("username not found for this user: "+username));
    }

    @Override
    public List<UserDtails> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDtails updateUser(UserDtails userDetails) {
        if (!userRepository.existsById(userDetails.getUserId())) {
            throw new ResourceNotFoundException("User", "id", userDetails.getUserId());
        }
        return userRepository.save(userDetails);
    }

    @Override
    public boolean existsByUsername(String username) {
        return false;
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
