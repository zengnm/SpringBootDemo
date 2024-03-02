package com.example.persistence;

import com.example.persistence.batch.BatchInsertRepository;
import com.example.persistence.entity.TestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TestDataRepository extends JpaRepository<TestData, Long>, BatchInsertRepository<TestData> {
    @Query(value = """
            select d.*
            from test_data d
            where d.id = :id
            """, nativeQuery = true)
    Optional<TestData> queryById(Long id);
}