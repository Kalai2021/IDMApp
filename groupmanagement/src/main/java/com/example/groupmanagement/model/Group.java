package com.example.groupmanagement.model;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder // Optional: for builder pattern

public class Group {
   
    private UUID id;
    private String name;
    private String displayName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Group(String name, String displayName, String description) {
        id = java.util.UUID.randomUUID();
        this.name = name;
        this.displayName = displayName;
        this.description = description;
    }   
}