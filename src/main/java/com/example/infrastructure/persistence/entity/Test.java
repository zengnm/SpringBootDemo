package com.example.infrastructure.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * test表对应entity
 *
 * @author zengnianmei
 */
@Data
@Entity
public class Test {
    @Id
    private Long id;
    private String name;
    private String userId;
    private String content;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
