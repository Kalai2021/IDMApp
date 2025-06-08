package com.example.rolemanagement.dto;

import java.time.LocalDateTime;

import lombok.*;
import java.util.UUID;
/**
 * RoleResponse is a DTO used to encapsulate the data returned when a role is fetched.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern   
public class RoleResponse {
    private UUID id;
    private String name;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    // Getters and setters
}