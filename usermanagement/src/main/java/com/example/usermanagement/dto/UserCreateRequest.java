package com.example.usermanagement.dto;
import lombok.*;


/**
 * UserCreateRequest is a DTO used to encapsulate the data required to create a new user.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class UserCreateRequest {
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    // Getters and setters
}