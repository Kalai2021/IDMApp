package com.example.orgmembermanagement.service;

import com.example.orgmembermanagement.model.OrgMember;
import com.example.orgmembermanagement.repository.OrgMemberRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class OrgMemberMgmtService {
    private static final Logger logger = LoggerFactory.getLogger(OrgMemberMgmtService.class);
    private final OrgMemberRepository orgMemberRepository;

    public OrgMemberMgmtService(OrgMemberRepository orgMemberRepository) {
        this.orgMemberRepository = orgMemberRepository;
    }

    public OrgMember addMember(UUID orgId, UUID entityId, String type) {
        logger.debug("Adding member to org: orgId={}, entityId={}, type={}", orgId, entityId, type);
        OrgMember orgMember = new OrgMember();
        orgMember.setOrgId(orgId);
        orgMember.setEntityId(entityId);
        orgMember.setType(type);
        return orgMemberRepository.save(orgMember);
    }

    public boolean removeMember(UUID orgId, UUID entityId) {
        logger.debug("Removing member from org: orgId={}, entityId={}", orgId, entityId);
        int deleted = orgMemberRepository.deleteByOrgIdAndEntityId(orgId, entityId);
        return deleted > 0;
    }
} 