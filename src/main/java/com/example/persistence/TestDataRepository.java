package com.example.persistence;

import com.example.persistence.batch.BatchInsertRepository;
import com.example.persistence.entity.TestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface TestDataRepository extends JpaRepository<TestData, Long>, BatchInsertRepository<TestData> {
    /**
     * 不能根据方法名自动查找namedQuery, namedMapping
     */
    @Query(name = "queryPart", nativeQuery = true)
    TestData queryPart(Long id);
}