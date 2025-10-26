package com.agriculture.smartagri.service;

import com.agriculture.smartagri.entity.User;
import com.agriculture.smartagri.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register new user with encoded password
    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    // Find user by username (used in login)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    // Check if email already exists
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Get all users by role
    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    // Get user by ID
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    // Update user
    public User updateUser(User user) {
        return userRepository.save(user);
    }

    // Delete user
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Get all users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Search users by name
    public List<User> searchUsersByName(String name) {
        return userRepository.findByNameContaining(name);
    }

    // Get users by role and location
    public List<User> getUsersByRoleAndLocation(String role, String location) {
        return userRepository.findByRoleAndLocationContaining(role, location);
    }

    // Count users by role
    public long countUsersByRole(String role) {
        return userRepository.countByRole(role);
    }

    // Check if username exists
    public boolean usernameExists(String username) {
        return userRepository.existsByUsername(username);
    }

    // Check if email exists
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Update user password
    public void updatePassword(Long userId, String newPassword) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }
}