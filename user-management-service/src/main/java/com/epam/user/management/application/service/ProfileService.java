package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.EditProfileRequest;
import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;

public interface ProfileService {
    public UserResponse getProfileByUsers(String email);
    public MessageResponse editUserProfile(String email, EditProfileRequest profileRequest);
}
