package com.epam.user.management.application.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditProfileRequest {
    private String firstName;
    private String lastName;
    private String password;
    private String gender;
    private String country;
    private String city;
    private String imageUrl;
    private String email;
}
