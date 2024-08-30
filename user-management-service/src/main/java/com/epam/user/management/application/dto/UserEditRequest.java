package com.epam.user.management.application.dto;

import lombok.Data;

@Data
public class UserEditRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String country;
    private String role;
    private String city;
}
