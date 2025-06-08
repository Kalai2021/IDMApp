package com.example.usergroupmanagement.model;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class Member {
    
    private UUID groupId;
    private UUID userId;
    private LocalDateTime createdAt;
    
    public Member(UUID groupId, UUID userId) {
        this.groupId = groupId;
        this.userId = userId;
    }
}