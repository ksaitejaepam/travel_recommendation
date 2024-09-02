package com.epam.user.management.application.controller;


import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;

import com.epam.user.management.application.repository.UserRepository;

import com.epam.user.management.application.service.JwtService;
import com.epam.user.management.application.service.ProfileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;


@RestController
@RequestMapping("/profile")
@Log
@CrossOrigin(origins="*")
public class ProfileController {

    private final JwtService jwtService;
    private final ProfileServiceImpl profileService;
    private final UserRepository userRepository;
    private ObjectMapper objectMapper;
    public ProfileController(JwtService jwtService , ProfileServiceImpl profileService , UserRepository userRepository) {
        this.jwtService = jwtService;
        this.profileService = profileService;
        this.userRepository=userRepository;
    }

    @GetMapping("/view")
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


    @PutMapping(value = "/editProfile",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MessageResponse> editUserProfile(@RequestParam("email") String email,
                                                           @RequestParam("firstName") String firstName,
                                                           @RequestParam("lastName") String lastName,
                                                           @RequestParam("password") String password,
                                                           @RequestParam("gender") String gender,
                                                           @RequestParam("country") String country,
                                                           @RequestParam("city") String city,
                                                           @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {
        profileService.updateUser(email, firstName, lastName, gender, password, country, city, file);
        return ResponseEntity.ok(MessageResponse.builder().message("Profile updated successfully").build());
    }



}
