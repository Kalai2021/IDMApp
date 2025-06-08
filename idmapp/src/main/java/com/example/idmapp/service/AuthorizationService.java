package com.example.idmapp.service;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientCheckRequest;
import dev.openfga.sdk.api.client.model.ClientCheckResponse;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    private final OpenFgaClient openFgaClient;

    public AuthorizationService(OpenFgaClient openFgaClient) {
        this.openFgaClient = openFgaClient;
    }

    public boolean checkAccess(String user, String relation, String object) {
        try {
            ClientCheckRequest request = new ClientCheckRequest()
                .user(user)
                .relation(relation)
                .object(object);

            ClientCheckResponse response = openFgaClient.check(request).get();
            return response.getAllowed();
        } catch (Exception e) {
            logger.error("Error checking authorization for user: {}, relation: {}, object: {}", 
                user, relation, object, e);
            return false;
        }
    }

    // Example methods for common authorization checks
    public boolean canManageUser(String userId, String targetUserId) {
        return checkAccess("user:" + userId, "can_manage", "user:" + targetUserId);
    }

    public boolean canManageGroup(String userId, String groupId) {
        return checkAccess("user:" + userId, "can_manage", "group:" + groupId);
    }

    public boolean canManageRole(String userId, String roleId) {
        return checkAccess("user:" + userId, "can_manage", "role:" + roleId);
    }

    public boolean canManageOrg(String userId, String orgId) {
        return checkAccess("user:" + userId, "can_manage", "org:" + orgId);
    }
} 