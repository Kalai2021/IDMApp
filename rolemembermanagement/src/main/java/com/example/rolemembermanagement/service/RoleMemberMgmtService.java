package com.example.rolemembermanagement.service;

import com.example.rolemembermanagement.model.RoleMember;
import com.example.rolemembermanagement.repository.RoleMemberRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
public class RoleMemberMgmtService {
    private static final Logger logger = LoggerFactory.getLogger(RoleMemberMgmtService.class);
    private final RoleMemberRepository roleMemberRepository;

    public RoleMemberMgmtService(RoleMemberRepository roleMemberRepository) {
        this.roleMemberRepository = roleMemberRepository;
    }

    public RoleMember addMember(UUID roleId, UUID entityId, String type) {
        logger.debug("Adding member to role: roleId={}, entityId={}, type={}", roleId, entityId, type);
        RoleMember roleMember = new RoleMember();
        roleMember.setRoleId(roleId);
        roleMember.setEntityId(entityId);
        roleMember.setType(type);
        return roleMemberRepository.save(roleMember);
    }

    public boolean removeMember(UUID roleId, UUID entityId) {
        logger.debug("Removing member from role: roleId={}, entityId={}", roleId, entityId);
        int deleted = roleMemberRepository.deleteByRoleIdAndEntityId(roleId, entityId);
        return deleted > 0;
    }
} 