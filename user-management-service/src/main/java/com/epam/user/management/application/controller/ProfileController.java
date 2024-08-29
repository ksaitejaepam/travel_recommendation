package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@Log
public class ProfileController {

    private final JwtService jwtService;
    private final AuthenticationService authService;

    public ProfileController(JwtService jwtService , AuthenticationService authService ) {
        this.jwtService = jwtService;
        this.authService = authService;
    }

    @GetMapping("/")
    public ResponseEntity<UserResponse> getProfile(HttpServletRequest request) {
        // Extract the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);

        UserResponse userResponse = authService.getProfileByUsers(email);
        if(userResponse == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(userResponse);

    }
}
