package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<UserResponse> getAllUsers();

    Optional<User> getUserById(Long id);

    User saveUser(User user);

    void deleteUser(Long id);

    Optional<User> getUserByEmail(String email);

    MessageResponse enableUser(Long id, String email);

    MessageResponse disableUser(Long id, String email);

    RegisterResponse create(UserCreateRequest userCreateRequest);

    MessageResponse updateUser(Long id,UserEditRequest userEditRequest);
}

