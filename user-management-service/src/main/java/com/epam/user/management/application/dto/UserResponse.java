package com.epam.user.management.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Component
public class UserResponse {
    private Long id;
//    private byte[] image;
    private String profileImageUrl;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String country;
    private String city;
    private boolean isEnabled;

}
