package com.example.usergroupmanagement.dto;

import java.time.LocalDateTime;
import java.util.UUID;


import lombok.*;
/**
 * memberResponse is a DTO used to encapsulate the data returned when a member is fetched.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern   
public class MemberOpResponse {
    private UUID id;
    private UUID groupId;
    private UUID userId;
    private LocalDateTime createdAt;
    // Getters and setters
}