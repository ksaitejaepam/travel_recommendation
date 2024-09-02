package com.epam.user.management.application;

import com.epam.user.management.application.dto.EditProfileRequest;
import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.repository.UserRepository;
import com.epam.user.management.application.service.ProfileServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class ProfileServiceTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
   private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProfileByUsers_Success() {
        // Setup mocks
        String email = "test@example.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .firstName("John")
                .lastName("Doe")
                .password("Password1@")
                .gender("Male")
                .country("USA")
                .city("New York")
                .role("User")
                .build();

        UserResponse userResponse = UserResponse.builder()
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .gender("Male")
                .country("USA")
                .city("New York")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(userResponse);

        // Call the method under test
        UserResponse response = profileService.getProfileByUsers(email);

        // Verify the results
        assertEquals(userResponse, response);
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testGetProfileByUsers_UserNotFound() {
        // Setup mocks
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Call the method under test and verify exception
        try {
            profileService.getProfileByUsers(email);
        } catch (UserNotFoundException e) {
            assertEquals("User not found with email: " + email, e.getMessage());
        }
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testGetProfileByUsers_UnauthorizedAccess() {
        // Setup mocks
        String email = "admin@example.com";

        User user = User.builder()
                .id(1L)
                .email(email)
                .firstName("Admin")
                .lastName("User")
                .password("Password1@")
                .gender("Male")
                .country("USA")
                .city("New York")
                .role("Admin")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Call the method under test and verify exception
        try {
            profileService.getProfileByUsers(email);
        } catch (UnauthorizedAccessException e) {
            assertEquals("Access denied for users with role: Admin", e.getMessage());
        }
        verify(userRepository).findByEmail(email);
    }

    @Test
    public void testEditUserProfile_Success() {
        // Setup mocks
        String email = "test@example.com";
        EditProfileRequest request = new EditProfileRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("Password1@");
        request.setGender("Male");
        request.setCountry("USA");
        request.setCity("New York");
        byte[] imageBytes = "imageData".getBytes();

        User user = User.builder()
                .id(1L)
                .email(email)
                .firstName("OldFirstName")
                .lastName("OldLastName")
                .password("OldPassword")
                .gender("OldGender")
                .country("OldCountry")
                .city("OldCity")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        MessageResponse expectedResponse = new MessageResponse("Profile updated successfully.");
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the method under test
        MessageResponse response = profileService.editUserProfile(email, request, imageBytes);

        // Verify the results
        assertEquals(expectedResponse.getMessage(), response.getMessage());
        verify(userRepository).save(user);
    }

    @Test
    public void testEditUserProfile_InvalidPassword() {
        // Setup mocks
        String email = "test@example.com";
        EditProfileRequest request = new EditProfileRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("short");
        request.setGender("Male");
        request.setCountry("USA");
        request.setCity("New York");

        // Call the method under test
        MessageResponse response = profileService.editUserProfile(email, request, null);

        // Verify the results
        assertEquals("Password does not meet the required criteria.", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void testEditUserProfile_UserNotFound() {
        // Setup mocks
        String email = "test@example.com";
        EditProfileRequest request = new EditProfileRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("Password1@");
        request.setGender("Male");
        request.setCountry("USA");
        request.setCity("New York");

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Call the method under test
        MessageResponse response = profileService.editUserProfile(email, request, null);

        // Verify the results
        assertEquals("Profile update failed: User not found", response.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }
}
