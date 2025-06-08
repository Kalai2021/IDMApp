package com.example.rolemanagement.service;

import com.example.rolemanagement.dto.*;
import com.example.rolemanagement.model.Role;
import com.example.rolemanagement.repository.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
/**
 * RoleService is a service class that handles the business logic for role management.
 * It interacts with the RoleRepository to perform CRUD operations on roles.
 */

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllroles() {
        return roleRepository.findAll();
    }

    public Optional<Role> getRoleById(UUID id) {
        return roleRepository.findById(id);
    }

    public Role createrole(RoleCreateRequest request) {
        Role role = new Role();
        role.setId(UUID.randomUUID());
        role.setName(request.getName());
        role.setDisplayName(request.getDisplayName());
        role.setDescription(request.getDescription());
        return roleRepository.save(role);
    }

    public Optional<Role> updaterole(UUID id, RoleUpdateRequest request) {
        Optional<Role> existingroleOpt = roleRepository.findById(id);
        if (existingroleOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Role role = existingroleOpt.get();
        role.setName(request.getName());
        role.setDisplayName(request.getDisplayName());
        role.setDescription(request.getDescription());
        
        int updated = roleRepository.update(role);
        if (updated > 0) {
            return Optional.of(role);
        }
        return Optional.empty();
    }

    public void deleteRole(UUID id) {
        roleRepository.deleteById(id);
    }
}