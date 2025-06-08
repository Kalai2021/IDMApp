package com.example.groupmanagement.service;

import com.example.groupmanagement.dto.*;
import com.example.groupmanagement.model.Group;
import com.example.groupmanagement.repository.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;

@Service
public class GroupService {
    private final GroupRepository groupRepository;

    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public List<Group> getAllgroups() {
        return groupRepository.findAll();
    }

    public Optional<Group> getGroupById(UUID id) {
        return groupRepository.findById(id);
    }

    public Group creategroup(GroupCreateRequest request) {
        Group group = new Group();
        group.setId(UUID.randomUUID());
        group.setName(request.getName());
        group.setDisplayName(request.getDisplayName());
        group.setDescription(request.getDescription());
        return groupRepository.save(group);
    }

    public Optional<Group> updategroup(UUID id, GroupUpdateRequest request) {
        Optional<Group> existinggroupOpt = groupRepository.findById(id);
        if (existinggroupOpt.isEmpty()) {
            return Optional.empty();
        }
        
        Group group = existinggroupOpt.get();
        group.setName(request.getName());
        group.setDisplayName(request.getDisplayName());
        group.setDescription(request.getDescription());
        
        int updated = groupRepository.update(group);
        if (updated > 0) {
            return Optional.of(group);
        }
        return Optional.empty();
    }

    public void deleteGroup(UUID id) {
        groupRepository.deleteById(id);
    }
}