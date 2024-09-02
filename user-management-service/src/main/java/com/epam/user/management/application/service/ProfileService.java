package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.EditProfileRequest;
import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.exception.UserNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProfileService {
    public UserResponse getProfileByUsers(String email);
    public void updateUser(String email , String firstName , String lastName , String gender,String password,String country , String city , MultipartFile file) throws IOException;



}
