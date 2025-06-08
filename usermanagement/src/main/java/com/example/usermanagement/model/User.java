package com.example.usermanagement.model;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class User {
   
    private UUID id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private boolean isActive = true;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public User(String name, String firstName, String lastName, String email, String password) {
        id = java.util.UUID.randomUUID();
        this.name = name;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        isActive = true;
    }   
}