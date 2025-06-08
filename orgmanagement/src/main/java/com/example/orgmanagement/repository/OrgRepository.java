package com.example.orgmanagement.repository;

import com.example.orgmanagement.model.Org;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository 
public class OrgRepository {
    private static final Logger logger = LoggerFactory.getLogger(OrgRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public OrgRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    private final RowMapper<Org> orgMapper = (rs, rowNum) -> {
        Org org = new Org();
        org.setId(UUID.fromString(rs.getString("id")));
        org.setName(rs.getString("name"));
        org.setDescription(rs.getString("description"));
        return org;
    };

    public Optional<Org> findById(UUID id) {
        String sql = "SELECT * FROM organizations WHERE id = ?";
        try {
            logger.debug("Finding org by ID: {}", id);
            return jdbcTemplate.query(sql, orgMapper, id).stream().findFirst();
        } catch (Exception e) {
            logger.error("Error finding org by ID: {}", id, e);
            return Optional.empty();
        }
    }

    public List<Org> findAll() {
        String sql = "SELECT * FROM organizations";
        try {
            logger.debug("Finding all orgs");
            List<Org> orgs = jdbcTemplate.query(sql, orgMapper);
            logger.debug("Found {} orgs", orgs.size());
            return orgs;
        } catch (Exception e) {
            logger.error("Error finding all orgs", e);
            return new ArrayList<>();
        }
    }

    public Org save(Org org) {
        if (org.getId() == null) {
            // Insert new org
            org.setId(UUID.randomUUID());
            String insertSql = "INSERT INTO organizations (id, name, description) VALUES (?, ?, ?)";
            try {
                logger.debug("Inserting new org: {}", org.getName());
                jdbcTemplate.update(insertSql,
                    org.getId(),
                    org.getName(),
                    org.getDescription()
                );
            } catch (Exception e) {
                logger.error("Error inserting org: {}", org.getName(), e);
                throw e;
            }
        } else {
            // Update existing org
            String updateSql = "UPDATE organizations SET name = ?, description = ? WHERE id = ?";
            try {
                logger.debug("Updating org: {}", org.getName());
                jdbcTemplate.update(updateSql,
                    org.getName(),
                    org.getDescription(),
                    org.getId()
                );
            } catch (Exception e) {
                logger.error("Error updating org: {}", org.getName(), e);
                throw e;
            }
        }
        return org;
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM organizations WHERE id = ?";
        try {
            logger.debug("Deleting org with ID: {}", id);
            int rowsDeleted = jdbcTemplate.update(sql, id);
            logger.debug("Deleted {} rows for org: {}", rowsDeleted, id);
            return rowsDeleted;
        } catch (Exception e) {
            logger.error("Error deleting org with ID: {}", id, e);
            throw e;
        }
    }
}