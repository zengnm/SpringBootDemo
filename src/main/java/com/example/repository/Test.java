package com.example.repository;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * test表Model
 *
 * @author zengnianmei
 */
public record Test(@Id Long id, String name, String userId, String content, LocalDateTime createTime,
                   LocalDateTime updateTime) {
}
