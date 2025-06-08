package com.example.orgmembermanagement.repository;

import com.example.orgmembermanagement.model.OrgMember;
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
public class OrgMemberRepository {
    private static final Logger logger = LoggerFactory.getLogger(OrgMemberRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public OrgMemberRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<OrgMember> orgMemberMapper = (rs, rowNum) -> {
        OrgMember orgMember = new OrgMember();
        orgMember.setId(UUID.fromString(rs.getString("id")));
        orgMember.setOrgId(UUID.fromString(rs.getString("org_id")));
        orgMember.setEntityId(UUID.fromString(rs.getString("entity_id")));
        orgMember.setType(rs.getString("type"));
        orgMember.setCreatedAt(rs.getTimestamp("created_at") != null ? rs.getTimestamp("created_at").toLocalDateTime() : null);
        return orgMember;
    };

    public Optional<OrgMember> findById(UUID id) {
        String sql = "SELECT * FROM org_members WHERE id = ?";
        try {
            logger.debug("Finding org member by ID: {}", id);
            return jdbcTemplate.query(sql, orgMemberMapper, id).stream().findFirst();
        } catch (Exception e) {
            logger.error("Error finding org member by ID: {}", id, e);
            return Optional.empty();
        }
    }

    public List<OrgMember> findByOrgId(UUID orgId) {
        String sql = "SELECT * FROM org_members WHERE org_id = ?";
        try {
            logger.debug("Finding org members by org ID: {}", orgId);
            return jdbcTemplate.query(sql, orgMemberMapper, orgId);
        } catch (Exception e) {
            logger.error("Error finding org members by org ID: {}", orgId, e);
            return new ArrayList<>();
        }
    }

    public OrgMember save(OrgMember orgMember) {
        orgMember.setId(UUID.randomUUID());
        String sql = "INSERT INTO org_members (id, org_id, entity_id, type) VALUES (?, ?, ?, ?)";
        try {
            logger.debug("Saving org member: {}", orgMember);
            jdbcTemplate.update(sql,
                orgMember.getId(),
                orgMember.getOrgId(),
                orgMember.getEntityId(),
                orgMember.getType()
            );
        } catch (Exception e) {
            logger.error("Error saving org member: {}", orgMember, e);
            throw e;
        }
        return orgMember;
    }

    public int deleteByOrgIdAndEntityId(UUID orgId, UUID entityId) {
        String sql = "DELETE FROM org_members WHERE org_id = ? AND entity_id = ?";
        try {
            logger.debug("Deleting org member with org ID: {} and entity ID: {}", orgId, entityId);
            return jdbcTemplate.update(sql, orgId, entityId);
        } catch (Exception e) {
            logger.error("Error deleting org member with org ID: {} and entity ID: {}", orgId, entityId, e);
            return 0;
        }
    }
} 