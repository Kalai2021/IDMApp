package com.example.orgmembermanagement.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrgMemberResponse {
    private UUID orgId;
    private UUID entityId;
    private String type;
    private LocalDateTime createdAt;
} 