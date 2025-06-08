package com.example.orgmanagement.controller;

import com.example.orgmanagement.dto.*;
import com.example.orgmanagement.model.Org;
import com.example.orgmanagement.service.OrgMgmtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class OrgController {
    private static final Logger logger = LoggerFactory.getLogger(OrgController.class);
    private final OrgMgmtService orgService;

    public OrgController(OrgMgmtService orgService) {
        this.orgService = orgService;
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
    public ResponseEntity<Org> updateOrg(@PathVariable(name = "id") UUID id, @RequestBody OrgUpdateResponse request) {
        logger.debug("Updating org with ID: {}", id);
        return orgService.updateOrg(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/orgs/{id}")
    public ResponseEntity<Void> deleteOrg(@PathVariable(name = "id") UUID id) {
        logger.debug("Deleting org with ID: {}", id);
        orgService.deleteOrg(id);
        return ResponseEntity.noContent().build();
    }
}