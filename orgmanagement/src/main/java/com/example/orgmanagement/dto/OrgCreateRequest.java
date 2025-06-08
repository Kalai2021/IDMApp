package com.example.orgmanagement.dto;
import lombok.*;


/**
 * OrgCreateRequest is a DTO used to encapsulate the data required to create a new Org.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class OrgCreateRequest {
    private String name;
    private String description;
    // Getters and setters
}