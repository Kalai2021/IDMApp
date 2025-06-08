package com.example.rolemanagement.repository;

import com.example.rolemanagement.model.Role;
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
public class RoleRepository {
    private static final Logger logger = LoggerFactory.getLogger(RoleRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public RoleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    private final RowMapper<Role> roleMapper = (rs, rowNum) -> {
        Role role = new Role();
        role.setId(UUID.fromString(rs.getString("id")));
        role.setName(rs.getString("name"));
        role.setDisplayName(rs.getString("display_name"));
        role.setDescription(rs.getString("description"));
        return role;
    };

    public Optional<Role> findById(UUID id) {
        String sql = "SELECT * FROM roles WHERE id = ?";
        try {
            logger.debug("Finding role by ID: {}", id);
            return jdbcTemplate.query(sql, roleMapper, id).stream().findFirst();
        } catch (Exception e) {
            logger.error("Error finding role by ID: {}", id, e);
            return Optional.empty();
        }
    }

    public List<Role> findAll() {
        String sql = "SELECT * FROM roles";
        try {
            logger.debug("Finding all roles");
            List<Role> roles = jdbcTemplate.query(sql, roleMapper);
            logger.debug("Found {} roles", roles.size());
            return roles;
        } catch (Exception e) {
            logger.error("Error finding all roles", e);
            return new ArrayList<>();
        }
    }

    public Role save(Role role) {
        String sql = "INSERT INTO roles (id, name, display_name, description) VALUES (?, ?, ?, ?)";
        try {
            logger.debug("Saving role: {}", role.getName());
            jdbcTemplate.update(sql,
                role.getId(),
                role.getName(),
                role.getDisplayName(),
                role.getDescription()
            );
            return role;
        } catch (Exception e) {
            logger.error("Error saving role: {}", role.getName(), e);
            throw e;
        }
    }

    public int update(Role role) {
        String sql = "UPDATE roles SET name = ?, display_name = ?, description = ? WHERE id = ?";
        try {
            logger.debug("Updating role: {}", role.getName());
            int rowsUpdated = jdbcTemplate.update(sql,
                role.getName(),
                role.getDisplayName(),
                role.getDescription(),
                role.getId()
            );
            logger.debug("Updated {} rows for role: {}", rowsUpdated, role.getId());
            return rowsUpdated;
        } catch (Exception e) {
            logger.error("Error updating role: {} with ID: {}", role.getName(), role.getId(), e);
            throw e;
        }
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM roles WHERE id = ?";
        try {
            logger.debug("Deleting role with ID: {}", id);
            int rowsDeleted = jdbcTemplate.update(sql, id);
            logger.debug("Deleted {} rows for role: {}", rowsDeleted, id);
            return rowsDeleted;
        } catch (Exception e) {
            logger.error("Error deleting role with ID: {}", id, e);
            throw e;
        }
    }
}