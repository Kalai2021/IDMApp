package com.example.usermanagement.dto;

import java.time.LocalDateTime;

import lombok.*;
/**
 * UserResponse is a DTO used to encapsulate the data returned when a user is fetched.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern   
public class UserResponse {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDateTime createdAt;

    // Getters and setters
}