package com.example.rolemanagement.dto;
import lombok.*;


/**
 * RoleCreateRequest is a DTO used to encapsulate the data required to create a new role.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class RoleCreateRequest {
    private String name;
    private String displayName;
    private String description;

    // Getters and setters
}