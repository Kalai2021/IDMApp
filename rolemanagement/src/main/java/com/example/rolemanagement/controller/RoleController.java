package com.example.rolemanagement.controller;

import com.example.rolemanagement.dto.*;
import com.example.rolemanagement.model.Role;
import com.example.rolemanagement.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping("/roles")
    public List<Role> getAllRoles() {
        System.out.println("Getting roles");
        return roleService.getAllroles();
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable(name = "id") UUID id) {
        System.out.println("Getting role by ID: " + id);
        return roleService.getRoleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@RequestBody RoleCreateRequest request) {
        return ResponseEntity.status(201).body(roleService.createrole(request));
    }

    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(name = "id") UUID id, @RequestBody RoleUpdateRequest request) {
        request.setId(id);
        return roleService.updaterole(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "id") UUID id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}