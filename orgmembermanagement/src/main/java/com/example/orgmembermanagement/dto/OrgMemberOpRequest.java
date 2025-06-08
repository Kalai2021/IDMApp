package com.example.orgmembermanagement.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class OrgMemberOpRequest {
    private int op; // 1 for ADD, 2 for REMOVE
    private String type; // USER, GROUP, or ROLE
    private UUID orgId;
    private UUID entityId;
} 