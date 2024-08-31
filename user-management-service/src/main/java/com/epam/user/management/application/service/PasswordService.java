package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;

public interface PasswordService {
    ForgotPasswordResponse isEmailPresent(String email);
    ResetPasswordResponse resetPassword(String userEmail, ResetPasswordRequest resetPasswordRequest);
}
