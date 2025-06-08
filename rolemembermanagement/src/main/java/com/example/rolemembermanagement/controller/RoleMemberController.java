package com.example.rolemembermanagement.controller;

import com.example.rolemembermanagement.model.RoleMember;
import com.example.rolemembermanagement.service.RoleMemberMgmtService;
import com.example.rolemembermanagement.dto.RoleMemberOpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/rolemembers")
public class RoleMemberController {
    private static final Logger logger = LoggerFactory.getLogger(RoleMemberController.class);
    private final RoleMemberMgmtService roleMemberMgmtService;

    public RoleMemberController(RoleMemberMgmtService roleMemberMgmtService) {
        this.roleMemberMgmtService = roleMemberMgmtService;
    }

    @PostMapping
    public ResponseEntity<?> handleMemberOperation(@RequestBody RoleMemberOpRequest request) {
        logger.debug("Received role member operation request: {}", request);
        
        if (request.getOp() == 1) { // ADD
            RoleMember roleMember = roleMemberMgmtService.addMember(
                request.getRoleId(),
                request.getEntityId(),
                request.getType()
            );
            return ResponseEntity.ok(roleMember);
        } else if (request.getOp() == 2) { // REMOVE
            boolean removed = roleMemberMgmtService.removeMember(
                request.getRoleId(),
                request.getEntityId()
            );
            if (removed) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.badRequest().body("Invalid operation code");
        }
    }
} 