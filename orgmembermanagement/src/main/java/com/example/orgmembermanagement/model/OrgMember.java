package com.example.orgmembermanagement.model;

import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Data
public class OrgMember {
    private UUID id;
    private UUID orgId;
    private UUID entityId;
    private String type; // USER, GROUP, or ROLE
    private LocalDateTime createdAt;
} 