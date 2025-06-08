package com.example.usergroupmanagement.dto;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class MemberOpRequest {

    private OpType op;
    private UUID groupId;
    private UUID userId;

    public MemberOpRequest(int op, UUID groupId, UUID userId) {
        this.op = OpType.fromValue(op);
        this.groupId = groupId;
        this.userId = userId;
    }
    
}


