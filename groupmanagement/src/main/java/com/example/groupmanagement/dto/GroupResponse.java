package com.example.groupmanagement.dto;

import java.time.LocalDateTime;

import lombok.*;
/**
 * GroupResponse is a DTO used to encapsulate the data returned when a group is fetched.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern   
public class GroupResponse {
    private String id;
    private String name;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;

    // Getters and setters
}