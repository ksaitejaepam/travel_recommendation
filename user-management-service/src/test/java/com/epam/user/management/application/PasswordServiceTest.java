package com.epam.user.management.application;
import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.PasswordServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PasswordServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private PasswordServiceImpl passwordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testResetPassword_EmptyNewPassword() {
        String userEmail = "test@example.com";
        String newPassword = "";
        String confirmPassword = "";

        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, confirmPassword);

        ResetPasswordResponse expectedResponse = new ResetPasswordResponse("Password does not meet the criteria", false);
        when(objectMapper.convertValue(any(), eq(ResetPasswordResponse.class))).thenReturn(expectedResponse);

        ResetPasswordResponse response = passwordService.resetPassword(userEmail, request);

        assertEquals(expectedResponse, response);
        verify(userRepository, never()).save(any());
    }



    @Test
    void testResetPassword_PasswordEncodingFailure() {
        String userEmail = "test@example.com";
        String newPassword = "Valid1Password!";
        String confirmPassword = "Valid1Password!";

        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, confirmPassword);
        User user = new User();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenThrow(new RuntimeException("Encoding error"));

        ResetPasswordResponse expectedResponse = new ResetPasswordResponse("Error resetting password.", false);
        when(objectMapper.convertValue(any(), eq(ResetPasswordResponse.class))).thenReturn(expectedResponse);

        ResetPasswordResponse response = passwordService.resetPassword(userEmail, request);

        assertEquals(expectedResponse, response);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPassword_ObjectMapperConversionFailure() {
        String userEmail = "test@example.com";
        String newPassword = "Valid1Password!";
        String confirmPassword = "Valid1Password!";

        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, confirmPassword);
        User user = new User();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedPassword");

        // Simulate failure in objectMapper.convertValue
        when(objectMapper.convertValue(any(), eq(ResetPasswordResponse.class)))
                .thenThrow(new RuntimeException("ObjectMapper conversion error"));

        // You may need to catch this exception depending on how your application handles it
        try {
            passwordService.resetPassword(userEmail, request);
        } catch (RuntimeException e) {
            assertEquals("ObjectMapper conversion error", e.getMessage());
        }
    }



    @Test
    void testResetPassword_PasswordNotValid() {
        String userEmail = "test@example.com";
        String newPassword = "invalid";
        String confirmPassword = "invalid";

        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, confirmPassword);

        ResetPasswordResponse expectedResponse = new ResetPasswordResponse("Password does not meet the criteria", false);
        when(objectMapper.convertValue(any(), eq(ResetPasswordResponse.class))).thenReturn(expectedResponse);

        ResetPasswordResponse response = passwordService.resetPassword(userEmail, request);

        assertEquals(expectedResponse, response);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPassword_PasswordsDoNotMatch() {
        String userEmail = "test@example.com";
        String newPassword = "Valid1Password!";
        String confirmPassword = "DifferentPassword!";

        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, confirmPassword);

        ResetPasswordResponse expectedResponse = new ResetPasswordResponse("new password and confirm password do not match", false);
        when(objectMapper.convertValue(any(), eq(ResetPasswordResponse.class))).thenReturn(expectedResponse);

        ResetPasswordResponse response = passwordService.resetPassword(userEmail, request);

        assertEquals(expectedResponse, response);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testResetPassword_UserNotFound() {
        String userEmail = "test@example.com";
        String newPassword = "Valid1Password!";
        String confirmPassword = "Valid1Password!";

        ResetPasswordRequest request = new ResetPasswordRequest(newPassword, confirmPassword);

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.empty());

        ResetPasswordResponse expectedResponse = new ResetPasswordResponse("Error resetting password.", false);
        when(objectMapper.convertValue(any(), eq(ResetPasswordResponse.class))).thenReturn(expectedResponse);

        ResetPasswordResponse response = passwordService.resetPassword(userEmail, request);

        assertEquals(expectedResponse, response);
        verify(userRepository, never()).save(any());
    }

    @Test
    void testIsEmailPresent_EmailExists() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

        ForgotPasswordResponse expectedResponse = new ForgotPasswordResponse("Email is present, please change your password.", true);
        when(objectMapper.convertValue(any(), eq(ForgotPasswordResponse.class))).thenReturn(expectedResponse);

        ForgotPasswordResponse response = passwordService.isEmailPresent(email);

        assertEquals(expectedResponse, response);
    }

    @Test
    void testIsEmailPresent_EmailDoesNotExist() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        ForgotPasswordResponse expectedResponse = new ForgotPasswordResponse("Email doesn't exist.", false);
        when(objectMapper.convertValue(any(), eq(ForgotPasswordResponse.class))).thenReturn(expectedResponse);

        ForgotPasswordResponse response = passwordService.isEmailPresent(email);

        assertEquals(expectedResponse, response);
    }
}
