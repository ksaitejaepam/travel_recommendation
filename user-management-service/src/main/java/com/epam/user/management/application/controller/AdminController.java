package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    private UserResponse userResponse;

    public AdminController(AdminService adminService,UserResponse userResponse){
        this.userResponse=userResponse;
        this.adminService=adminService;
    }

    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        return adminService.getAllUsers();
    }

    @PutMapping("/users/{id}/enable")
    public void enableUser(@PathVariable Long id) {
        Optional<User> userOptional = adminService.getUserById(id);
        userOptional.ifPresent(user -> {
            user.setEnabled(true);
            adminService.saveUser(user);
        });
    }

    @PutMapping("/users/{id}/disable")
    public void disableUser(@PathVariable Long id) {
        Optional<User> userOptional = adminService.getUserById(id);
        userOptional.ifPresent(user -> {
            user.setEnabled(false);
            adminService.saveUser(user);
        });
    }
}