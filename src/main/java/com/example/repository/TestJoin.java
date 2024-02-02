package com.example.repository;

import org.springframework.data.annotation.Id;

/**
 * @author zengnianmei
 */
public record TestJoin(@Id Long id, String name, String dataName) {
}
