package com.example.groupmanagement.dto;
import lombok.*;


/**
 * GroupCreateRequest is a DTO used to encapsulate the data required to create a new group.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class GroupCreateRequest {
    private String name;
    private String displayName;
    private String description;

    // Getters and setters
}