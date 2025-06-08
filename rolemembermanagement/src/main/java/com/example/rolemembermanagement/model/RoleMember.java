package com.example.rolemembermanagement.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class RoleMember {
    private UUID id;
    private UUID roleId;
    private UUID entityId;
    private String type; // USER or GROUP
    private LocalDateTime createdAt;
} 