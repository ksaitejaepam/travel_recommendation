package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl extends AdminService{

    private UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminServiceImpl(PasswordEncoder passwordEncoder,UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository=userRepository;
    }

    public List<UserResponse> getAllUsers() {
        List<User> users = userRepository.findByRole("User");
        return users.stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setEmail(user.getUsername());
                    response.setFirstName(user.getFirstName());
                    response.setLastName(user.getLastName());
                    response.setGender(user.getGender());
                    response.setCountry(user.getCountry());
                    response.setCity(user.getCity());
                    response.setEnabled(user.isEnabled());
                    response.setImageUrl(user.getImageUrl());
                    return response;
                })
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public MessageResponse enableUser(Long id, String email) {
        Optional<User> currentUser = getUserByEmail(email);
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
            if (currentUser.isPresent() && "Admin".equals(currentUser.get().getRole())) {
                User user = userOptional.get();
                user.setEnabled(true);
                saveUser(user);
                return new MessageResponse("User enabled successfully");
            } else {
                return new MessageResponse("You are not authorized for this action");
            }
        } else {
            return new MessageResponse("User not found");
        }
    }

    public MessageResponse disableUser(Long id, String email) {
        Optional<User> currentUser = getUserByEmail(email);
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
            if (currentUser.isPresent() && "Admin".equals(currentUser.get().getRole())) {
                User user = userOptional.get();
                user.setEnabled(false);
                saveUser(user);
                return new MessageResponse("User disabled successfully");
            } else {
                return new MessageResponse("You are not authorized for this action");
            }
        } else {
            return new MessageResponse("User not found");
        }
    }

    public RegisterResponse create(UserCreateRequest userCreateRequest) {
        Optional<User> user = userRepository.findByEmail(userCreateRequest.getEmail());

        if (user.isPresent()) {
            return RegisterResponse.builder().message("User with email already exists").build();
        } else {
            User newUser = User.builder()
                    .firstName(userCreateRequest.getFirstName())
                    .lastName(userCreateRequest.getLastName())
                    .email(userCreateRequest.getEmail())
                    .password(passwordEncoder.encode(userCreateRequest.getPassword()))
                    .gender(userCreateRequest.getGender())
                    .country(userCreateRequest.getCountry())
                    .city(userCreateRequest.getCity())
                    .role(userCreateRequest.getRole())
                    .isEnabled(true)
                    .build();
            userRepository.save(newUser);
            return RegisterResponse.builder().message("User Created successful").build();
        }
    }
    public MessageResponse updateUser(Long id,UserEditRequest userEditRequest) {
        Optional<User> userOptional = getUserById(id);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userEditRequest.getFirstName());
            user.setLastName(userEditRequest.getLastName());
            user.setEmail(userEditRequest.getEmail());
            user.setGender(userEditRequest.getGender());
            user.setCountry(userEditRequest.getCountry());
            user.setCity(userEditRequest.getCity());
            user.setRole(userEditRequest.getRole());
            userRepository.save(user);
            return new MessageResponse("User updated successfully");
        } else {
            return new MessageResponse("User not found");
        }
    }
}
