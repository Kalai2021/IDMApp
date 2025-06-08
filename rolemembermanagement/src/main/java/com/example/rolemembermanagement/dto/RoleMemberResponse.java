package com.example.rolemembermanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RoleMemberResponse {
    private UUID roleId;
    private UUID entityId;
    private String type;
    private LocalDateTime createdAt;
} 