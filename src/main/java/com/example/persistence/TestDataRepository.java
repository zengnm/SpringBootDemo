package com.example.persistence;

import com.example.persistence.batch.BatchInsertRepository;
import com.example.persistence.entity.TestData;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.AvailableHints;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


public interface TestDataRepository extends JpaRepository<TestData, Long>, BatchInsertRepository<TestData> {
    /**
     * 不能根据方法名自动查找namedQuery, namedMapping
     */
    @Query(name = "queryPart", nativeQuery = true)
    TestData queryPart(Long id);

    @Modifying
    @Query(value = """
            insert into test_data(name, status)
            values (:name, :status)
            """, nativeQuery = true)
    int batchInsertNative(List<String> name, List<Integer> status);

    @Query(value = """
            SELECT *
            FROM test_data
            WHERE id = :id
            """, nativeQuery = true)
    Optional<TestData> findByIdNoCache(Long id);

    @Transactional
    @Modifying
    @Query(value = """
            UPDATE test_data
            SET name=:name
            WHERE id=:id
            """, nativeQuery = true)
    int updateNameById(Long id, String name);
}