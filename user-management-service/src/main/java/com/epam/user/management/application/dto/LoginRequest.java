package com.epam.user.management.application.dto;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
