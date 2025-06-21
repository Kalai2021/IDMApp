package com.example.sharedsecurity.service;

import dev.openfga.sdk.api.client.OpenFgaClient;
import dev.openfga.sdk.api.client.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    private final OpenFgaClient fgaClient;

    public AuthorizationService(OpenFgaClient fgaClient) {
        this.fgaClient = fgaClient;
    }

    /**
     * Sample usage: Check if a user has a relation to an object in OpenFGA
     * @param user the user (e.g., "user:123")
     * @param relation the relation (e.g., "member")
     * @param object the object (e.g., "group:456")
     * @return true if allowed, false otherwise
     */
    public boolean checkAccess(String userId, String relation, String resourceId) {
        try {
            var checkRequest = new ClientCheckRequest()
                    .user(userId)
                    .relation(relation)
                    ._object(resourceId);
            
            CompletableFuture<ClientCheckResponse> future = fgaClient.check(checkRequest);
            ClientCheckResponse response = future.get();
            
            boolean allowed = response.getAllowed();
            logger.debug("Permission check: {} {} {} -> {}", userId, relation, resourceId, 
                        allowed ? "ALLOWED" : "DENIED");
            
            return allowed;
        } catch (Exception e) {
            logger.error("Error checking permission for user: {}, relation: {}, resource: {}", 
                        userId, relation, resourceId, e);
            return false;
        }
    }



    // Example methods for common authorization checks
    public boolean canManageUser(String userId, String targetUserId) {
        return false; // Placeholder implementation
    }

    public boolean canManageGroup(String userId, String groupId) {
        return false; // Placeholder implementation
    }

    public boolean canManageRole(String userId, String roleId) {
        return false; // Placeholder implementation
    }

    public boolean canManageOrg(String userId, String orgId) {
        return false; // Placeholder implementation
    }

       /**
     * Grant permission to a user for a specific resource
     * 
     * @param userId The user identifier
     * @param relation The relation/permission to grant
     * @param resourceId The resource identifier
     * @return true if the permission was granted successfully
     */
    public boolean grantPermission(String userId, String relation, String resourceId) {
        try {
            var tuple = new ClientTupleKey()
                    .user(userId)
                    .relation(relation)
                    ._object(resourceId);
            
            var writeRequest = new ClientWriteRequest()
                    .writes(List.of(tuple));
            
            CompletableFuture<ClientWriteResponse> future = fgaClient.write(writeRequest);
            future.get();
            
            logger.info("Granted permission: {} {} {}", userId, relation, resourceId);
            return true;
        } catch (Exception e) {
            logger.error("Error granting permission for user: {}, relation: {}, resource: {}", 
                        userId, relation, resourceId, e);
            return false;
        }
    }
    
    /**
     * Revoke permission from a user for a specific resource
     * 
     * @param userId The user identifier
     * @param relation The relation/permission to revoke
     * @param resourceId The resource identifier
     * @return true if the permission was revoked successfully
     */
    public boolean revokePermission(String userId, String relation, String resourceId) {
        try {
            var tuple = new ClientTupleKeyWithoutCondition()
                    .user(userId)
                    .relation(relation)
                    ._object(resourceId);
            
            var writeRequest = new ClientWriteRequest()
                    .deletes(List.of(tuple));
            
            CompletableFuture<ClientWriteResponse> future = fgaClient.write(writeRequest);
            future.get();
            
            logger.info("Revoked permission: {} {} {}", userId, relation, resourceId);
            return true;
        } catch (Exception e) {
            logger.error("Error revoking permission for user: {}, relation: {}, resource: {}", 
                        userId, relation, resourceId, e);
            return false;
        }
    }
    
    /**
     * Get all resources of a specific type that a user has a certain relation to
     * 
     * @param userId The user identifier
     * @param relation The relation/permission
     * @param resourceType The type of resource (e.g., "document", "folder")
     * @return List of resource identifiers the user has access to
     */
    public List<String> getAccessibleResources(String userId, String relation, String resourceType) {
        try {
            var listObjectsRequest = new ClientListObjectsRequest()
                    .user(userId)
                    .relation(relation)
                    .type(resourceType);
            
            CompletableFuture<ClientListObjectsResponse> future = fgaClient.listObjects(listObjectsRequest);
            ClientListObjectsResponse response = future.get();
            
            List<String> resources = response.getObjects();
            logger.debug("User {} has {} access to {} resources of type {}", 
                        userId, relation, resources.size(), resourceType);
            
            return resources;
        } catch (Exception e) {
            logger.error("Error getting accessible resources for user: {}, relation: {}, type: {}", 
                        userId, relation, resourceType, e);
            return List.of();
        }
    }
    
    /**
     * Get all users who have a specific relation to a resource
     * 
     * @param resourceId The resource identifier
     * @param relation The relation/permission
     * @return List of user identifiers who have the relation to the resource
     */
    public List<String> getUsersWithAccess(String resourceId, String relation) {
        try {
            var readRequest = new ClientReadRequest()
                    ._object(resourceId)
                    .relation(relation);
            
            CompletableFuture<ClientReadResponse> future = fgaClient.read(readRequest);
            ClientReadResponse response = future.get();
            
            List<String> users = response.getTuples().stream()
                    .map(tuple -> tuple.getKey().getUser())
                    .collect(Collectors.toList());
            
            logger.debug("Found {} users with {} access to {}", users.size(), relation, resourceId);
            return users;
        } catch (Exception e) {
            logger.error("Error getting users with access to resource: {}, relation: {}", 
                        resourceId, relation, e);
            return List.of();
        }
    }
    
    /**
     * Check multiple permissions at once
     * 
     * @param userId The user identifier
     * @param checks List of permission checks (relation, resourceId pairs)
     * @return List of boolean results corresponding to each check
     */
    public List<Boolean> checkMultiplePermissions(String userId, List<PermissionCheck> checks) {
        return checks.stream()
                .map(check -> checkAccess(userId, check.getRelation(), check.getResourceId()))
                .collect(Collectors.toList());
    }
    
    /**
     * Batch grant multiple permissions
     * 
     * @param grants List of permission grants to apply
     * @return true if all permissions were granted successfully
     */
    public boolean batchGrantPermissions(List<PermissionGrant> grants) {
        try {
            List<ClientTupleKey> tuples = grants.stream()
                    .map(grant -> new ClientTupleKey()
                            .user(grant.getUserId())
                            .relation(grant.getRelation())
                            ._object(grant.getResourceId()))
                    .collect(Collectors.toList());
            
            var writeRequest = new ClientWriteRequest()
                    .writes(tuples);
            
            CompletableFuture<ClientWriteResponse> future = fgaClient.write(writeRequest);
            future.get();
            
            logger.info("Batch granted {} permissions", grants.size());
            return true;
        } catch (Exception e) {
            logger.error("Error batch granting permissions", e);
            return false;
        }
    }
    
    /**
     * Batch revoke multiple permissions
     * 
     * @param revocations List of permission revocations to apply
     * @return true if all permissions were revoked successfully
     */
    public boolean batchRevokePermissions(List<PermissionGrant> revocations) {
        try {
            List<ClientTupleKeyWithoutCondition> tuples = revocations.stream()
                    .map(revocation -> new ClientTupleKeyWithoutCondition()
                            .user(revocation.getUserId())
                            .relation(revocation.getRelation())
                            ._object(revocation.getResourceId()))
                    .collect(Collectors.toList());
            
            var writeRequest = new ClientWriteRequest()
                    .deletes(tuples);
            
            CompletableFuture<ClientWriteResponse> future = fgaClient.write(writeRequest);
            future.get();
            
            logger.info("Batch revoked {} permissions", revocations.size());
            return true;
        } catch (Exception e) {
            logger.error("Error batch revoking permissions", e);
            return false;
        }
    }
    
    /**
     * Check if a user can perform any of the specified actions on a resource
     * 
     * @param userId The user identifier
     * @param relations List of relations to check
     * @param resourceId The resource identifier
     * @return true if the user has at least one of the permissions
     */
    public boolean hasAnyPermission(String userId, List<String> relations, String resourceId) {
        return relations.stream()
                .anyMatch(relation -> checkAccess(userId, relation, resourceId));
    }
    
    /**
     * Check if a user has all of the specified permissions on a resource
     * 
     * @param userId The user identifier
     * @param relations List of relations to check
     * @param resourceId The resource identifier
     * @return true if the user has all of the permissions
     */
    public boolean hasAllPermissions(String userId, List<String> relations, String resourceId) {
        return relations.stream()
                .allMatch(relation -> checkAccess(userId, relation, resourceId));
    }
    
    // Helper classes for batch operations
    public static class PermissionCheck {
        private final String relation;
        private final String resourceId;
        
        public PermissionCheck(String relation, String resourceId) {
            this.relation = relation;
            this.resourceId = resourceId;
        }
        
        public String getRelation() { return relation; }
        public String getResourceId() { return resourceId; }
    }
    
    public static class PermissionGrant {
        private final String userId;
        private final String relation;
        private final String resourceId;
        
        public PermissionGrant(String userId, String relation, String resourceId) {
            this.userId = userId;
            this.relation = relation;
            this.resourceId = resourceId;
        }
        
        public String getUserId() { return userId; }
        public String getRelation() { return relation; }
        public String getResourceId() { return resourceId; }
    }
} 