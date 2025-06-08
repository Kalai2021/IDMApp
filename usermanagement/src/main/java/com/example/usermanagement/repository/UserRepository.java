package com.example.usermanagement.repository;

import com.example.usermanagement.model.User;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.core.BeanPropertyRowMapper; // For mapping rows to User object

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;
import java.util.UUID;

@Repository 

public class UserRepository {
    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }   

    private final RowMapper<User> userMapper = (rs, rowNum) -> {
        User user = new User();
        user.setId(UUID.fromString(rs.getString("id")));
        user.setName(rs.getString("name"));
        user.setFirstName(rs.getString("firstname"));
        user.setLastName(rs.getString("lastname"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        return user;
    };

    public Optional<User> findById(UUID id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            return jdbcTemplate.query(sql, userMapper, id).stream().findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try {
            return jdbcTemplate.query(sql, userMapper, email).stream().findFirst();
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public List<User> findAll() {
        String sql = "SELECT * FROM users";
        try {
            return jdbcTemplate.query(sql, userMapper);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public User save(User user) {
        String sql = "INSERT INTO users (id, name, firstname, lastname, email, password) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            jdbcTemplate.update(sql,
                user.getId(),
                user.getName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword()
            );
            return user;
        } catch (org.springframework.dao.DuplicateKeyException e) {
            if (e.getMessage().contains("users_name_key")) {
                throw new RuntimeException("User with name '" + user.getName() + "' already exists");
            } else if (e.getMessage().contains("users_email_key")) {
                throw new RuntimeException("User with email '" + user.getEmail() + "' already exists");
            }
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    public int update(User user) {
        String sql = "UPDATE users SET name = ?, firstname = ?, lastname = ?, email = ?, password = ? WHERE id = ?";
        try {
            return jdbcTemplate.update(sql,
                user.getName(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPassword(),
                user.getId()
            );
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int deleteById(UUID id) {
        String sql = "DELETE FROM users WHERE id = ?";
        try {
            return jdbcTemplate.update(sql, id);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}