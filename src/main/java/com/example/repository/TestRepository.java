package com.example.repository;

import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

/**
 * test表Repository
 *
 * @author zengnianmei
 */
public interface TestRepository extends ReactiveCrudRepository<Test, Long>, ReactiveQueryByExampleExecutor<Test> {
}
