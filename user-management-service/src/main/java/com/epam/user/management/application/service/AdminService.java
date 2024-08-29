package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<UserResponse> getAllUsers() {
        List<User> users=userRepository.findAll();
        return users.stream()
                .map(user -> {
                    UserResponse response = new UserResponse();
                    response.setId(user.getId());
                    response.setEmail(user.getUsername()); // Assuming username is used as email
                    response.setFirstname(user.getFirstName()); // Adjust based on actual field names
                    response.setLastname(user.getLastName()); // Adjust based on actual field names
                    response.setGender(user.getGender()); // Adjust based on actual field names
                    response.setCountry(user.getCountry()); // Adjust based on actual field names
                    response.setCity(user.getCity()); // Adjust based on actual field names
                    response.setEnabled(user.isEnabled());
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
}
