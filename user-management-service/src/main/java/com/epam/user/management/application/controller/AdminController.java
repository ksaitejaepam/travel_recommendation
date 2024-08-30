package com.epam.user.management.application.controller;
import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.service.AdminServiceImpl;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController{

    @Autowired
    private AdminServiceImpl adminServiceImpl;
    private JwtService jwtService;
    private final AuthenticationService authenticationService;

    public AdminController(AdminServiceImpl adminServiceImpl, JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.adminServiceImpl = adminServiceImpl;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminServiceImpl.getAllUsers();
    }

    @PutMapping("/users/{id}/enable")
    public ResponseEntity<MessageResponse> enableUser(@PathVariable Long id, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);
        MessageResponse response = adminServiceImpl.enableUser(id, email);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{id}/disable")
    public ResponseEntity<MessageResponse> disableUser(@PathVariable Long id, HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authorizationHeader.substring(7);
        String email = jwtService.extractUsername(token);
        MessageResponse response = adminServiceImpl.disableUser(id, email);
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "/users/create", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RegisterResponse> create(@RequestBody UserCreateRequest userCreateRequest) {

        RegisterResponse registerResponse = adminServiceImpl.create(userCreateRequest);

        return ResponseEntity.ok(registerResponse);
    }
    @PutMapping("/users/{id}/update")
    public ResponseEntity<MessageResponse> updateUser(@PathVariable Long id, @RequestBody UserEditRequest userEditRequest) {
        MessageResponse response = adminServiceImpl.updateUser(id,userEditRequest);
        return ResponseEntity.ok(response);
    }

}
 