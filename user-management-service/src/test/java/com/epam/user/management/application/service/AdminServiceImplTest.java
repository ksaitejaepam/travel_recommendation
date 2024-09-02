package com.epam.user.management.application.service;

import com.epam.user.management.application.dto.*;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setGender("Male");
        user.setCountry("Country");
        user.setCity("City");
        user.setEnabled(true);
        user.setImageUrl("http://example.com/image.jpg");
        user.setRole("User");

        when(userRepository.findByRole("User")).thenReturn(Arrays.asList(user));

        List<UserResponse> users = adminService.getAllUsers();

        assertNotNull(users);
        assertEquals(1, users.size());
        UserResponse response = users.get(0);
        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("John", response.getFirstName());
        assertEquals("Doe", response.getLastName());
        assertEquals("Male", response.getGender());
        assertEquals("Country", response.getCountry());
        assertEquals("City", response.getCity());
        assertTrue(response.isEnabled());
        assertEquals("http://example.com/image.jpg", response.getImageUrl());
    }

    @Test
    void testEnableUserSuccess() {
        User admin = new User();
        admin.setRole("Admin");
        User user = new User();
        user.setId(1L);
        user.setEnabled(false);

        when(userRepository.findByEmail("admin@example.com")).thenReturn(Optional.of(admin));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MessageResponse response = adminService.enableUser(1L, "admin@example.com");

        assertEquals("User enabled successfully", response.getMessage());
        verify(userRepository).save(user);
        assertTrue(user.isEnabled());
    }

    @Test
    void testEnableUserNotAuthorized() {
        User nonAdmin = new User();
        nonAdmin.setRole("User");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(nonAdmin));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        MessageResponse response = adminService.enableUser(1L, "user@example.com");

        assertEquals("You are not authorized for this action", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testCreateUserSuccess() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("newuser@example.com");
        request.setFirstName("Jane");
        request.setLastName("Doe");
        request.setPassword("password");
        request.setGender("Female");
        request.setCountry("Country");
        request.setCity("City");
        request.setRole("User");

        when(userRepository.findByEmail("newuser@example.com")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        RegisterResponse response = adminService.create(request);

        assertEquals("User registration successful", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testCreateUserAlreadyExists() {
        UserCreateRequest request = new UserCreateRequest();
        request.setEmail("existinguser@example.com");

        when(userRepository.findByEmail("existinguser@example.com")).thenReturn(Optional.of(new User()));

        RegisterResponse response = adminService.create(request);

        assertEquals("User with email already exists", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testUpdateUserSuccess() {
        UserEditRequest request = new UserEditRequest();
        request.setFirstName("UpdatedFirstName");
        request.setLastName("UpdatedLastName");
        request.setEmail("updated@example.com");
        request.setGender("Female");
        request.setCountry("NewCountry");
        request.setCity("NewCity");
        request.setRole("User");

        User user = new User();
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        MessageResponse response = adminService.updateUser(1L, request);

        assertEquals("User updated successfully", response.getMessage());
        verify(userRepository).save(user);
        assertEquals("UpdatedFirstName", user.getFirstName());
        assertEquals("UpdatedLastName", user.getLastName());
        assertEquals("updated@example.com", user.getEmail());
        assertEquals("Female", user.getGender());
        assertEquals("NewCountry", user.getCountry());
        assertEquals("NewCity", user.getCity());
        assertEquals("User", user.getRole());
    }

    @Test
    void testUpdateUserNotFound() {
        UserEditRequest request = new UserEditRequest();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        MessageResponse response = adminService.updateUser(1L, request);

        assertEquals("User not found", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
