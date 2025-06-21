package com.example.orgmanagement.dto;

import java.time.LocalDateTime;

import lombok.*;
/**
 * OrgResponse is a DTO used to encapsulate the data returned when a Org is fetched.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern   
public class OrgResponse {
    private String id;
    private String name;
    private String description;
 
    // Getters and setters
}