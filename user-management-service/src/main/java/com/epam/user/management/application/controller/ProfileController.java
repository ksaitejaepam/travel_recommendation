package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.EditProfileRequest;
import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.ProfileServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@Log
@CrossOrigin
public class ProfileController {

    private final JwtService jwtService;
    private final ProfileServiceImpl profileService;

    public ProfileController(JwtService jwtService , ProfileServiceImpl profileService ) {
        this.jwtService = jwtService;
        this.profileService = profileService;
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<?> getProfile(HttpServletRequest request) {
        // Extract the Authorization header
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
           // return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Authorization header missing or invalid"));
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);

        UserResponse userResponse = profileService.getProfileByUsers(email);

        if(userResponse == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("User Not Found"));
        }
        return ResponseEntity.ok(userResponse);

    }

    @PutMapping("/editProfile")
    public ResponseEntity<MessageResponse> editUserProfile(@AuthenticationPrincipal UserDetails userDetails,
                                            @RequestBody EditProfileRequest profileRequest) {
        String userName = userDetails.getUsername(); // username is the email
        MessageResponse response = profileService.editUserProfile(userName, profileRequest);

        if (response.getMessage().startsWith("Password does not meet")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        return ResponseEntity.ok(response);
    }

}
