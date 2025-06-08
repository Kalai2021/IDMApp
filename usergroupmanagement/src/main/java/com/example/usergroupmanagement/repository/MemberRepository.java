package com.example.usergroupmanagement.repository;

import com.example.usergroupmanagement.model.Member;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;


@Repository 

public class MemberRepository {
    private final JdbcTemplate jdbcTemplate;

    public MemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    private final RowMapper<Member> memberMapper = (rs, rowNum) -> {
        Member member = new Member();
        //member.setId(UUID.fromString(rs.getString("id"))); // Ensure this matches the column name in your DB
        member.setGroupId(rs.getString("group_id") != null ? UUID.fromString(rs.getString("group_id")) : null);
        member.setUserId(rs.getString("user_id") != null ? UUID.fromString(rs.getString("user_id")) : null);
        member.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        
        return member;
    };


    public Member findByGroupIdAndUserId(UUID groupId, UUID userId) {
        String sql = "SELECT * FROM user_groups WHERE group_id = ? AND user_id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{groupId, userId}, memberMapper);
    }

    public List<Member> findAll() {
        String sql = "SELECT * FROM user_groups";
        return jdbcTemplate.query(sql, memberMapper);
    }

    public List<Member> findByGroupId(UUID groupId) {
        String sql = "SELECT * FROM user_groups WHERE group_id = ?";
        return jdbcTemplate.query(sql, new Object[]{groupId}, memberMapper);
    }

    public List<Member> findByUserId(UUID userId) {
        String sql = "SELECT * FROM user_groups WHERE user_id = ?";
        return jdbcTemplate.query(sql, new Object[]{userId}, memberMapper);
    }


    public Member save(Member member) {
        String sql = "INSERT INTO user_groups ( group_id, user_id) VALUES (?, ?, ?, ?)";   
        int id = jdbcTemplate.update(sql,  member.getGroupId(), member.getUserId());
        return member;
    }

    public Member addMember ( int type, UUID groupId, UUID userId) {
        return save(new Member(groupId, userId));
        
    }

    public boolean removeMember(UUID groupId, UUID userId) {
        String sql = "DELETE FROM user_groups (group_id, user_id)  WHERE VALUES (?,?)";
        try {
            return jdbcTemplate.update(sql, groupId, userId) > 0;
        } catch (Exception e) {
            // Handle exception, return false or rethrow
            return false;
        }
    }

    public int deleteById(UUID groupId, UUID userId) {
        String sql = "DELETE FROM user_groups WHERE group_id = ? AND user_id = ?";
        try {
            return jdbcTemplate.update(sql, groupId, userId);
        } catch (Exception e) {
            // Handle exception, return 0 or rethrow
            return 0;
        }
    }
}