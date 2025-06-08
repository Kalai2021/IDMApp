package com.example.rolemembermanagement.repository;

import com.example.rolemembermanagement.model.RoleMember;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository
public class RoleMemberRepository {
    private static final Logger logger = LoggerFactory.getLogger(RoleMemberRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public RoleMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<RoleMember> roleMemberMapper = (rs, rowNum) -> {
        RoleMember roleMember = new RoleMember();
        roleMember.setId(UUID.fromString(rs.getString("id")));
        roleMember.setRoleId(UUID.fromString(rs.getString("role_id")));
        roleMember.setEntityId(UUID.fromString(rs.getString("entity_id")));
        roleMember.setType(rs.getString("type"));
        roleMember.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return roleMember;
    };

    public Optional<RoleMember> findById(UUID id) {
        String sql = "SELECT * FROM role_members WHERE id = ?";
        try {
            logger.debug("Finding role member by ID: {}", id);
            return jdbcTemplate.query(sql, roleMemberMapper, id).stream().findFirst();
        } catch (Exception e) {
            logger.error("Error finding role member by ID: {}", id, e);
            return Optional.empty();
        }
    }

    public List<RoleMember> findByRoleId(UUID roleId) {
        String sql = "SELECT * FROM role_members WHERE role_id = ?";
        try {
            logger.debug("Finding role members by role ID: {}", roleId);
            return jdbcTemplate.query(sql, roleMemberMapper, roleId);
        } catch (Exception e) {
            logger.error("Error finding role members by role ID: {}", roleId, e);
            return new ArrayList<>();
        }
    }

    public RoleMember save(RoleMember roleMember) {
        roleMember.setId(UUID.randomUUID());
        String sql = "INSERT INTO role_members (id, role_id, entity_id, type) VALUES (?, ?, ?, ?)";
        try {
            logger.debug("Saving role member: {}", roleMember);
            jdbcTemplate.update(sql,
                roleMember.getId(),
                roleMember.getRoleId(),
                roleMember.getEntityId(),
                roleMember.getType()
            );
        } catch (Exception e) {
            logger.error("Error saving role member: {}", roleMember, e);
            throw e;
        }
        return roleMember;
    }

    public int deleteByRoleIdAndEntityId(UUID roleId, UUID entityId) {
        String sql = "DELETE FROM role_members WHERE role_id = ? AND entity_id = ?";
        try {
            logger.debug("Deleting role member with role ID: {} and entity ID: {}", roleId, entityId);
            return jdbcTemplate.update(sql, roleId, entityId);
        } catch (Exception e) {
            logger.error("Error deleting role member with role ID: {} and entity ID: {}", roleId, entityId, e);
            return 0;
        }
    }
} 