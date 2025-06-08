package com.example.usermanagement.dto;

import lombok.*;
/**
 * AuthResponse is a DTO used to encapsulate the authentication response data,
 * typically containing a token and its expiration time.
 */
@Getter
@Setter
@AllArgsConstructor

public class AuthResponse {
    private String token;
    private long expiresIn;

    // Getters and setters
}