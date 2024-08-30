package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.EditProfileRequest;
import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class ProfileServiceImpl implements ProfileService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private ObjectMapper objectMapper;

   // private static final String PASSWORD_PATTERN = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";
    private static final String PASSWORD_PATTERN = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,12}$";



    public ProfileServiceImpl(UserRepository userRepository,PasswordEncoder passwordEncoder,ObjectMapper objectMapper)
    {
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.objectMapper=objectMapper;
    }

    private boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    @Override
    public UserResponse getProfileByUsers(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(user.isPresent()) {
            if ("User".equalsIgnoreCase(user.get().getRole())) {
                return user.map(value -> objectMapper.convertValue(value, UserResponse.class)).orElse(null);
            } else {
                throw new UnauthorizedAccessException("Access denied for users with role: " + user.get().getRole());
            }
        }
        else{
            throw new UserNotFoundException("User not found with email: "+email);
        }
    }

    @Transactional
    @Override
    public MessageResponse editUserProfile(String userName, EditProfileRequest profileRequest) {
        if (!isPasswordValid(profileRequest.getPassword())) {
            return new MessageResponse("Password does not meet the required criteria.");
        }
        try
        {
            Optional <User> optionalUser = userRepository.findByEmail(userName);
            User user;
            if(optionalUser.isPresent() ){
                user=optionalUser.get();
                user.setFirstName(profileRequest.getFirstName());
                user.setLastName(profileRequest.getLastName());
                user.setPassword(passwordEncoder.encode(profileRequest.getPassword()));
                user.setGender(profileRequest.getGender());
                user.setCountry(profileRequest.getCountry());
                user.setCity(profileRequest.getCity());
                user.setImageUrl(profileRequest.getImageUrl());
            }
            else {
                throw new UserNotFoundException("User not found");
            }
            userRepository.save(user);
            return new MessageResponse("Profile updated successfully.");
        }
        catch (Exception e) {
            // Log the exception details here for debugging
            return new MessageResponse("Profile update failed: " + e.getMessage());
        }
    }
}
