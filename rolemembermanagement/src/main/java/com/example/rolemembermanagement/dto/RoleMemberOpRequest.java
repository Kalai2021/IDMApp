package com.example.rolemembermanagement.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RoleMemberOpRequest {
    private int op; // 1 for ADD, 2 for REMOVE
    private String type; // USER or GROUP
    private UUID roleId;
    private UUID entityId;
} 