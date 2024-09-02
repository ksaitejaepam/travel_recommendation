package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.ForgotPasswordResponse;
import com.epam.user.management.application.dto.ResetPasswordRequest;
import com.epam.user.management.application.dto.ResetPasswordResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class PasswordServiceImpl implements PasswordService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#])[A-Za-z\\d@$!%*?&#]{8,12}$";


    public PasswordServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            ObjectMapper objectMapper

    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.objectMapper = objectMapper;
    }
    private boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }
    @Transactional
    @Override
    public ResetPasswordResponse resetPassword(String userEmail,ResetPasswordRequest resetPasswordRequest) {

        String newPassword = resetPasswordRequest.getNewPassword();
        String confirmPassword = resetPasswordRequest.getConfirmPassword();

        if(!newPassword.equals(confirmPassword)){
            return objectMapper.convertValue(new ResetPasswordResponse("new password and confirm password do not match",false),
                    ResetPasswordResponse.class);
        }
        if (!isPasswordValid(newPassword)) {
            return objectMapper.convertValue(
                    new ResetPasswordResponse("Password does not meet the criteria", false),
                    ResetPasswordResponse.class
            );
        }


        Optional<User> userOptional = userRepository.findByEmail(userEmail);
        if(userOptional.isPresent()){
            User userEntity = userOptional.get();
            userEntity.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(userEntity);
            return objectMapper.convertValue(new ResetPasswordResponse("password has been reset successfully",true),
                    ResetPasswordResponse.class);
        }
        return objectMapper.convertValue(new ResetPasswordResponse("Error resetting password.",false),
                ResetPasswordResponse.class);
    }
    @Override
    public ForgotPasswordResponse isEmailPresent(String email) {
        boolean emailExists = userRepository.findByEmail(email).isPresent();
        String message = emailExists ? "Email is present, please change your password."
                                    : "Email doesn't exist.";
        return objectMapper.convertValue(new ForgotPasswordResponse(message,emailExists),
                ForgotPasswordResponse.class);
    }

}
