package com.epam.user.management.application;

import com.epam.user.management.application.service.AuthenticationService;
import com.epam.user.management.application.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LogoutTest {
    private AuthenticationService authenticationService;
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBlacklistToken() {
        String token = "sampleToken";

        // Blacklist the token
        jwtService.blacklistToken(token);

        // Verify that the token is blacklisted
        assertTrue(jwtService.isTokenBlacklisted(token));
    }

    @Test
    void testIsTokenBlacklisted_True() {
        String token = "sampleToken";

        // Add the token to the blacklist
        jwtService.blacklistToken(token);

        // Verify that the token is blacklisted
        assertTrue(jwtService.isTokenBlacklisted(token));
    }

    @Test
    void testIsTokenBlacklisted_False() {
        String token = "sampleToken";

        // Verify that a token not in the blacklist returns false
        assertFalse(jwtService.isTokenBlacklisted(token));
    }
}
