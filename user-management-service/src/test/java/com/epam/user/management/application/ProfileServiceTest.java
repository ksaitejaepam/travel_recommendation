package com.epam.user.management.application;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.epam.user.management.application.exception.UnauthorizedAccessException;
import com.epam.user.management.application.exception.UserNotFoundException;
import com.epam.user.management.application.service.ProfileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import com.epam.user.management.application.entity.User;
import com.epam.user.management.application.dto.EditProfileRequest;
import com.epam.user.management.application.dto.UserResponse;
import com.epam.user.management.application.dto.MessageResponse;
import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private ProfileServiceImpl profileService;

    private User user;
    private UserResponse userResponse;
    private EditProfileRequest profileRequest;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .imageUrl("http://example.com/image.jpg")
                .gender("Male")
                .country("Country")
                .city("City")
                .isEnabled(true)
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .email("test@example.com")
                .firstName("Test")
                .lastName("User")
                .imageUrl("http://example.com/image.jpg")
                .gender("Male")
                .country("Country")
                .city("City")
                .isEnabled(true)
                .build();

        profileRequest = new EditProfileRequest();
        profileRequest.setFirstName("Updated");
        profileRequest.setLastName("User");
        profileRequest.setPassword("newPassword");
        profileRequest.setGender("Male");
        profileRequest.setCountry("UpdatedCountry");
        profileRequest.setCity("UpdatedCity");
        profileRequest.setImageUrl("http://example.com/updated_image.jpg");

    }

    @Test
    public void testGetProfileByUsers_withUserRole_returnsUserResponse() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setRole("User");
        UserResponse userResponse = new UserResponse();
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
        when(objectMapper.convertValue(user, UserResponse.class)).thenReturn(userResponse);

        // Act
        UserResponse result = profileService.getProfileByUsers(email);

        // Assert
        assertNotNull(result, "UserResponse should not be null");
        assertEquals(userResponse, result, "The UserResponse should match the expected response");
    }
    @Test
    public void testGetProfileByUsers_UserFound_WithDifferentRole() {
        String email = "admin@example.com";
        User user = User.builder()
                .email(email)
                .role("Admin")
                .firstName("Jane")
                .lastName("Doe")
                .build();

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));

        UnauthorizedAccessException thrown = assertThrows(
                UnauthorizedAccessException.class,
                () -> profileService.getProfileByUsers(email)
        );

        assertEquals("Access denied for users with role: Admin", thrown.getMessage());
    }

    @Test
    public void testGetProfileByUsers_UserNotFound() {
        String email = "nonexistent@example.com";
        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.empty());

        UserNotFoundException thrown = assertThrows(
            UserNotFoundException.class,
            () -> profileService.getProfileByUsers(email)
        );

        assertEquals("User not found with email: " + email, thrown.getMessage());
    }
    @Test
    void editUserProfile_Success() {
        String email="test@example.com";

        when(userRepository.findByEmail(eq(email))).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");
        // Arrange
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        MessageResponse response = profileService.editUserProfile("test@example.com", profileRequest);

        // Assert
        assertEquals("Profile updated successfully.", response.getMessage());
        verify(userRepository).save(user);
        assertEquals("Updated", user.getFirstName()); // Verify first name was updated correctly
    }

    @Test
    void testEditUserProfile_UserNotFound() {
        // Arrange
        String unknownEmail = "unknown@example.com";
        EditProfileRequest profileRequest = new EditProfileRequest();
        when(userRepository.findByEmail(eq(unknownEmail))).thenReturn(Optional.empty());
        MessageResponse response = profileService.editUserProfile(unknownEmail, profileRequest);
        assertNotNull(response);
        assertEquals("Profile update failed: User not found", response.getMessage());

    }
}