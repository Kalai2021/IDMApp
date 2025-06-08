package com.example.groupmanagement.controller;

import com.example.groupmanagement.dto.*;
import com.example.groupmanagement.model.Group;
import com.example.groupmanagement.service.GroupService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class GroupController {
    private final GroupService groupService;

    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping("/groups")
    public List<Group> getAllGroups() {
        System.out.println("Getting groups");
        return groupService.getAllgroups();
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<Group> getGroupById(@PathVariable(name = "id") UUID id) {
        System.out.println("Getting group by ID: " + id);
        return groupService.getGroupById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/groups")
    public ResponseEntity<Group> createGroup(@RequestBody GroupCreateRequest request) {
        return ResponseEntity.status(201).body(groupService.creategroup(request));
    }

    @PutMapping("/groups/{id}")
    public ResponseEntity<Group> updateGroup(@PathVariable(name = "id") UUID id, @RequestBody GroupUpdateRequest request) {
        return groupService.updategroup(id, request)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/groups/{id}")
    public ResponseEntity<Void> deleteGroup(@PathVariable(name = "id") UUID id) {
        groupService.deleteGroup(id);
        return ResponseEntity.noContent().build();
    }
}