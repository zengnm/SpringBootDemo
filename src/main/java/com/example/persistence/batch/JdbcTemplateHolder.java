package com.example.persistence.batch;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JdbcTemplateHolder {
    public static JdbcTemplate jdbcTemplate;
    public JdbcTemplateHolder(JdbcTemplate jdbcTemplate) {
        JdbcTemplateHolder.jdbcTemplate = jdbcTemplate;
    }
}
