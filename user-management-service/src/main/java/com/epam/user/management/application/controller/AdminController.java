package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;



    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/users/{id}/enable")
    public ResponseEntity<MessageResponse> enableUser(@RequestBody Long id) {
        Optional<User> userOptional = adminService.getUserById(id);
        if (userOptional.isPresent()) {
            userOptional.ifPresent(user -> {
                user.setEnabled(true);
                adminService.saveUser(user);
            });
            return ResponseEntity.ok(new MessageResponse("User enabled successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
        }
    }

    @PutMapping("/users/{id}/disable")
    public ResponseEntity<MessageResponse> disableUser(@RequestBody Long id) {
        Optional<User> userOptional = adminService.getUserById(id);
        if (userOptional.isPresent()) {

            userOptional.ifPresent(user -> {
                user.setEnabled(false);
                adminService.saveUser(user);
            });
            return ResponseEntity.ok(new MessageResponse("User disabled successfully"));
        } else {
            return ResponseEntity.badRequest().body(new MessageResponse("User not found"));
        }
    }
}