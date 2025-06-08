package com.example.orgmanagement.service;

import com.example.orgmanagement.dto.*;
import com.example.orgmanagement.model.Org;
import com.example.orgmanagement.repository.OrgRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class OrgMgmtService {
    private static final Logger logger = LoggerFactory.getLogger(OrgMgmtService.class);
    private final OrgRepository orgRepository;

    public OrgMgmtService(OrgRepository orgRepository) {
        this.orgRepository = orgRepository;
    }

    public List<Org> getAllOrgs() {
        return orgRepository.findAll();
    }

    public Optional<Org> getOrgById(UUID id) {
        return orgRepository.findById(id);
    }

    public Org createOrg(OrgCreateRequest request) {
        Org org = new Org();
        org.setName(request.getName());
        org.setDescription(request.getDescription());
        return orgRepository.save(org);
    }

    public Optional<Org> updateOrg(UUID id, OrgUpdateResponse request) {
        Optional<Org> existingOrgOpt = orgRepository.findById(id);
        if (existingOrgOpt.isEmpty()) {
            return Optional.empty();
        }
        
        existingOrgOpt.ifPresent(org -> {
            org.setName(request.getName());
            org.setDescription(request.getDescription());
            orgRepository.save(org);
        });
        return existingOrgOpt;
    }

    public void deleteOrg(UUID id) {
        orgRepository.deleteById(id);
    }
}