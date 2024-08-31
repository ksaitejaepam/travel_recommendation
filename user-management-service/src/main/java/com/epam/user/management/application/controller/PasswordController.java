package com.epam.user.management.application.controller;

import com.epam.user.management.application.dto.ForgotPasswordRequest;
import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.service.PasswordServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    public PasswordServiceImpl passwordService;

    private static String userEmail;
    @PostMapping("/forgot")
    public ResponseEntity<ForgotPasswordResponse> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        ForgotPasswordResponse response = passwordService.isEmailPresent(forgotPasswordRequest.getEmail());
        if (response.isEmailExists()) {
            userEmail= forgotPasswordRequest.getEmail();
            return ResponseEntity.ok(new ForgotPasswordResponse("Email is present, please change your password.",true));
        } else {
            return ResponseEntity.ok(new ForgotPasswordResponse("Email doesn't exist.",false));
        }
    }
    @PostMapping("/reset")
    public ResponseEntity<ResetPasswordResponse> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        ResetPasswordResponse response = passwordService.resetPassword(userEmail,resetPasswordRequest);
        if(!response.isSuccess()){
            return ResponseEntity.ok(new ResetPasswordResponse("Error resetting password.",false));
        }
        return ResponseEntity.ok(new ResetPasswordResponse("password has been reset successfully",true));
    }
}
