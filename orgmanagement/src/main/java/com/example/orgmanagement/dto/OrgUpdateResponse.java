package com.example.orgmanagement.dto;

import lombok.*;
/**
 * OrgUpdateRequest is a DTO used to encapsulate the data required to update an existing Org.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
 
public class OrgUpdateResponse {
    private String id;
    private String name;
    private String description;
    // Getters and setters
}