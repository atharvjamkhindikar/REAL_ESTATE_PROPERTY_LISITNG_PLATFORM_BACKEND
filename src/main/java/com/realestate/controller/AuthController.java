package com.realestate.controller;

import com.realestate.dto.ApiResponse;
import com.realestate.dto.LoginRequest;
import com.realestate.dto.LoginResponse;
import com.realestate.model.User;
import com.realestate.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<User> userOptional = userService.getUserByEmail(loginRequest.getEmail());
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
        }
        
        User user = userOptional.get();
        
        // Check password (in production, use BCrypt or similar)
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error("Invalid email or password"));
        }
        
        // Check if user is active
        if (!user.getActive()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(ApiResponse.error("Your account has been deactivated. Please contact support."));
        }
        
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .role(user.getRole())
                .subscriptionType(user.getSubscriptionType())
                .active(user.getActive())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success("Login successful", loginResponse));
    }
    
    @GetMapping("/me/{id}")
    public ResponseEntity<ApiResponse<LoginResponse>> getCurrentUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.getUserById(id);
        
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error("User not found"));
        }
        
        User user = userOptional.get();
        
        LoginResponse loginResponse = LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .userType(user.getUserType())
                .role(user.getRole())
                .subscriptionType(user.getSubscriptionType())
                .active(user.getActive())
                .build();
        
        return ResponseEntity.ok(ApiResponse.success(loginResponse));
    }
}
