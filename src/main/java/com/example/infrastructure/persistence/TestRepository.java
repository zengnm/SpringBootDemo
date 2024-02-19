package com.example.infrastructure.persistence;

import com.example.infrastructure.persistence.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * test表crud
 *
 * @author zengnianmei
 */
public interface TestRepository extends JpaRepository<Test, Long> {

    @Query(value = """
            SELECT id, name, user_id, content, create_time, update_time
            FROM test
            WHERE name like ?1
            LIMIT 10
            """, nativeQuery = true)
    List<Test> querySql(String nameMatch);
}
