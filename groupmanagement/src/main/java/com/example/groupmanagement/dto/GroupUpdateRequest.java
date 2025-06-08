package com.example.groupmanagement.dto;

import lombok.*;
/**
 * GroupUpdateRequest is a DTO used to encapsulate the data required to update an existing group.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdateRequest {
    private String name;
    private String displayName;
    private String description;
}