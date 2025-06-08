package com.example.groupmanagement.repository;

import com.example.groupmanagement.model.Group;
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
public class GroupRepository {
    private static final Logger logger = LoggerFactory.getLogger(GroupRepository.class);
    private final JdbcTemplate jdbcTemplate;

    public GroupRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    private final RowMapper<Group> groupMapper = (rs, rowNum) -> {
        Group group = new Group();
        group.setId(UUID.fromString(rs.getString("id")));
        group.setName(rs.getString("name"));
        group.setDisplayName(rs.getString("displayname"));
        group.setDescription(rs.getString("description"));
        return group;
    };

    public Optional<Group> findById(UUID id) {
        String sql = "SELECT * FROM groups WHERE id = ?";
        try {
            logger.debug("Finding group by ID: {}", id);
            return jdbcTemplate.query(sql, groupMapper, id).stream().findFirst();
        } catch (Exception e) {
            logger.error("Error finding group by ID: {}", id, e);
            return Optional.empty();
        }
    }

    public List<Group> findAll() {
        String sql = "SELECT * FROM groups";
        try {
            return jdbcTemplate.query(sql, groupMapper);
        } catch (Exception e) {
            logger.error("Error finding all groups", e);
            return new ArrayList<>();
        }
    }

    public Group save(Group group) {
        String sql = "INSERT INTO groups (id, name, displayname, description) VALUES (?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            group.getId(),
            group.getName(),
            group.getDisplayName(),
            group.getDescription()
        );
        return group;
    }

    public int update(Group group) {
        String sql = "UPDATE groups SET name = ?, displayname = ?, description = ? WHERE id = ?";
        try {
            return jdbcTemplate.update(sql,
                group.getName(),
                group.getDisplayName(),
                group.getDescription(),
                group.getId()
            );
        } catch (Exception e) {
            logger.error("Error updating group: {}", group.getId(), e);
            return 0;
        }
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM groups WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            logger.error("Error deleting group: {}", id, e);
            return 0;
        }
    }
}