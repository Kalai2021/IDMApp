package com.example.usermanagement.dto;

import lombok.*;
/**
 * LoginRequest is a DTO used to encapsulate the data required for user login.
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {
    private String email;
    private String password;

    // Getters and setters
}