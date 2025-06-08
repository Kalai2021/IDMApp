package com.example.orgmembermanagement.controller;

import com.example.orgmembermanagement.model.OrgMember;
import com.example.orgmembermanagement.service.OrgMemberMgmtService;
import com.example.orgmembermanagement.dto.OrgMemberOpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orgmembers")
public class OrgMemberController {
    private static final Logger logger = LoggerFactory.getLogger(OrgMemberController.class);
    private final OrgMemberMgmtService orgMemberMgmtService;

    public OrgMemberController(OrgMemberMgmtService orgMemberMgmtService) {
        this.orgMemberMgmtService = orgMemberMgmtService;
    }

    @PostMapping
    public ResponseEntity<?> handleMemberOperation(@RequestBody OrgMemberOpRequest request) {
        logger.debug("Received org member operation request: {}", request);
        
        if (request.getOp() == 1) { // ADD
            OrgMember orgMember = orgMemberMgmtService.addMember(
                request.getOrgId(),
                request.getEntityId(),
                request.getType()
            );
            return ResponseEntity.ok(orgMember);
        } else if (request.getOp() == 2) { // REMOVE
            boolean removed = orgMemberMgmtService.removeMember(
                request.getOrgId(),
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