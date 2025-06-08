package com.example.usermanagement.dto;

import lombok.*;
/**
 * UserUpdateRequest is a DTO used to encapsulate the data required to update an existing user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
 
public class UserUpdateRequest {
    private String username;
    private String email;
    private String password;

    // Getters and setters
}