package com.example.usergroupmanagement.service;

import com.example.usergroupmanagement.dto.MemberOpRequest;  
import com.example.usergroupmanagement.model.Member;
import com.example.usergroupmanagement.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.List;


@Service
public class MemberMgmtService {
    private final MemberRepository memberRepository;

    public MemberMgmtService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public Member getMember(UUID groupId, UUID userId) {
        if (groupId == null || userId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findByGroupIdAndUserId(groupId, userId);
    }

    public List<Member> getMembers() {
        return memberRepository.findAll();
    }

    public List<Member> getMembersByGroupId(UUID groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findByGroupId(groupId);
    }
    public List<Member> getMembersByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findByUserId(userId);
    }
    
    public Member getMemberByGroupIdAndUserId(UUID groupId, UUID userId) {
        if (groupId == null || userId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findByGroupIdAndUserId(groupId, userId);
    }
 
    /**
     * public Member getMemberByGroupId(UUID groupId) {
        if (groupId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findByGroupId(groupId);
    }

    public Member getMemberByUserId(UUID userId) {
        if (userId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findByUserId(userId);
    }

    public Member getMemberById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        return memberRepository.findById(id).orElse(null);
    }

    **/
    public Member addMember(MemberOpRequest request) {
        Member member = new Member();
        member.setGroupId(request.getGroupId()); // Assuming groupId is the groupId
        member.setUserId(request.getUserId()); // Assuming userId is the user
        //member.setId(java.util.UUID.randomUUID()); // Generate a new UUID for the member
        member.setCreatedAt(java.time.LocalDateTime.now()); // Set createdAt to current time
        return memberRepository.save(member); // Assuming save method in memberRepository handles insertion
    }

    public void deleteMember(UUID groupId, UUID userId) {
        if (groupId == null || userId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        memberRepository.removeMember(groupId, userId);
    }
}