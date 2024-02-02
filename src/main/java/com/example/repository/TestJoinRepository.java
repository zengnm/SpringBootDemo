package com.example.repository;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import reactor.core.publisher.Flux;

/**
 * @author zengnianmei
 */
public interface TestJoinRepository extends Repository<TestJoin, Long> {

    @Query("""
            SELECT t.id as id, t.name as name, d.name as data_name
            FROM test_data d join test t
            WHERE d.id = t.id and t.name like concat(:name, '%')
            """)
    Flux<TestJoin> query(String name);
}
