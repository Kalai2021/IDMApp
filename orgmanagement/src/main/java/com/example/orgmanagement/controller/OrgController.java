package com.example.orgmanagement.controller;

import com.example.orgmanagement.dto.*;
import com.example.orgmanagement.model.Org;
import com.example.orgmanagement.service.OrgMgmtService;
import com.example.sharedsecurity.service.AuthorizationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrgController {
    private static final Logger logger = LoggerFactory.getLogger(OrgController.class);
    private final OrgMgmtService orgService;
    private final AuthorizationService authorizationService;

    @Autowired
    public OrgController(OrgMgmtService orgService, AuthorizationService authorizationService) {
        this.orgService = orgService;
        this.authorizationService = authorizationService;
    }

    @GetMapping("/orgs")
    public List<Org> getAllOrgs() {
        logger.debug("Getting all orgs");
        return orgService.getAllOrgs();
    }

    @GetMapping("/orgs/{id}")
    public ResponseEntity<Org> getOrgById(@PathVariable(name = "id") UUID id) {
        logger.debug("Getting org by ID: {}", id);
        return orgService.getOrgById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/orgs")
    public ResponseEntity<Org> createOrg(@RequestBody OrgCreateRequest request) {
        logger.debug("Creating new org: {}", request.getName());
        return ResponseEntity.status(201).body(orgService.createOrg(request));
    }

    @PutMapping("/orgs/{id}")
    public ResponseEntity<Org> updateOrg(@PathVariable(name = "id") UUID id, @RequestBody OrgUpdateResponse request, @RequestHeader("Authorization") String authHeader) {
        logger.debug("Updating org with ID: {}", id);
        // TODO: Extract userId from JWT or SecurityContext
        String userId = "demo-user-id"; // Replace with real extraction
        String user = "user:" + userId;
        String relation = "admin"; // or whatever relation is required
        String object = "org:" + id;
        boolean allowed = authorizationService.checkAccess(user, relation, object);
        if (!allowed) {
            return ResponseEntity.status(403).body(null);
        }
        return orgService.updateOrg(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/orgs/{id}")
    public ResponseEntity<Void> deleteOrg(@PathVariable(name = "id") UUID id, @RequestHeader("Authorization") String authHeader) {
        logger.debug("Deleting org with ID: {}", id);
        // TODO: Extract userId from JWT or SecurityContext
        String userId = "demo-user-id"; // Replace with real extraction
        String user = "user:" + userId;
        String relation = "admin"; // or whatever relation is required
        String object = "org:" + id;
        boolean allowed = authorizationService.checkAccess(user, relation, object);
        if (!allowed) {
            return ResponseEntity.status(403).build();
        }
        orgService.deleteOrg(id);
        return ResponseEntity.noContent().build();
    }

    public boolean canViewOrg(String userId, String orgId) {
        return authorizationService.checkAccess("user:" + userId, "viewer", "org:" + orgId);
    }
}