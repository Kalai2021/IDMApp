package com.example.rolemanagement.dto;

import lombok.*;
import java.util.UUID;
/**
 * RoleUpdateRequest is a DTO used to encapsulate the data required to update an existing role.
 */
@Getter
@Setter
@AllArgsConstructor
public class RoleUpdateRequest {
    private UUID id;
    private String name;
    private String displayName;
    private String description;
}
