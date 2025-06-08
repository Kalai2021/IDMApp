package com.example.orgmanagement.model;

import java.time.LocalDateTime;
import java.util.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Org {
    private UUID id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public Org(String name, String description) {
        id = UUID.randomUUID();
        this.name = name;
        this.description = description;
    }
}