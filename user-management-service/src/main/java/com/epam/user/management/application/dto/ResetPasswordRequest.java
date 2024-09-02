package com.epam.user.management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResetPasswordRequest {
    private String newPassword;
    private String confirmPassword;
}
