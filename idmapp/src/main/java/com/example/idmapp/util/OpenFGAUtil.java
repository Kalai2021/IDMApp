package com.example.idmapp.util;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.ClientWriteRequest;
import dev.openfga.sdk.api.client.model.TupleKey;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OpenFGAUtil {
    private static final Logger logger = LoggerFactory.getLogger(OpenFGAUtil.class);
    private final OpenFgaClient openFgaClient;

    public OpenFGAUtil(OpenFgaClient openFgaClient) {
        this.openFgaClient = openFgaClient;
    }

    public void createUserManagementTuple(String managerId, String targetUserId) {
        try {
            ClientWriteRequest request = new ClientWriteRequest()
                .writes(List.of(
                    new TupleKey()
                        .user("user:" + managerId)
                        .relation("can_manage_user")
                        .object("user:" + targetUserId)
                ));

            openFgaClient.write(request).get();
        } catch (Exception e) {
            logger.error("Error creating user management tuple for manager: {} and target: {}", 
                managerId, targetUserId, e);
        }
    }

    public void createGroupManagementTuple(String managerId, String groupId) {
        try {
            ClientWriteRequest request = new ClientWriteRequest()
                .writes(List.of(
                    new TupleKey()
                        .user("user:" + managerId)
                        .relation("can_manage_group")
                        .object("group:" + groupId)
                ));

            openFgaClient.write(request).get();
        } catch (Exception e) {
            logger.error("Error creating group management tuple for manager: {} and group: {}", 
                managerId, groupId, e);
        }
    }

    public void createRoleManagementTuple(String managerId, String roleId) {
        try {
            ClientWriteRequest request = new ClientWriteRequest()
                .writes(List.of(
                    new TupleKey()
                        .user("user:" + managerId)
                        .relation("can_manage_role")
                        .object("role:" + roleId)
                ));

            openFgaClient.write(request).get();
        } catch (Exception e) {
            logger.error("Error creating role management tuple for manager: {} and role: {}", 
                managerId, roleId, e);
        }
    }

    public void createOrgManagementTuple(String managerId, String orgId) {
        try {
            ClientWriteRequest request = new ClientWriteRequest()
                .writes(List.of(
                    new TupleKey()
                        .user("user:" + managerId)
                        .relation("can_manage_org")
                        .object("org:" + orgId)
                ));

            openFgaClient.write(request).get();
        } catch (Exception e) {
            logger.error("Error creating org management tuple for manager: {} and org: {}", 
                managerId, orgId, e);
        }
    }

    public void deleteManagementTuple(String managerId, String objectType, String objectId) {
        try {
            ClientWriteRequest request = new ClientWriteRequest()
                .deletes(List.of(
                    new TupleKey()
                        .user("user:" + managerId)
                        .relation("can_manage_" + objectType)
                        .object(objectType + ":" + objectId)
                ));

            openFgaClient.write(request).get();
        } catch (Exception e) {
            logger.error("Error deleting management tuple for manager: {}, type: {}, id: {}", 
                managerId, objectType, objectId, e);
        }
    }
} 