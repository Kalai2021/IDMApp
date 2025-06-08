package com.example.usergroupmanagement.controller;

import com.example.usergroupmanagement.dto.*;
import com.example.usergroupmanagement.model.Member;
import com.example.usergroupmanagement.service.MemberMgmtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class MemberController {
    private final MemberMgmtService memberService;

    public MemberController(MemberMgmtService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/groupmembers")
    public ResponseEntity<Member> addMember(@RequestBody MemberOpRequest request) {
        if (request.getGroupId() == null || request.getUserId() == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        // Assuming addMember method in memberService handles insertion
        // You can return a response indicating success or the created member
        if ( request.getOp() == OpType.ADD) {
            return ResponseEntity.status(201).body(memberService.addMember(request));
        }
        if ( request.getOp() == OpType.REMOVE) {
            memberService.deleteMember(request.getGroupId(), request.getUserId());
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.status(201).body(memberService.addMember(request));
    }

    @GetMapping("/groupmembers")
    public List<Member> getMembers() {
       
        return memberService.getMembers();

    }

    @GetMapping("/groupmembers/{groupId}")
    public List<Member> getMembersByGroupId(@PathVariable UUID groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberService.getMembersByGroupId(groupId);
    }
}    