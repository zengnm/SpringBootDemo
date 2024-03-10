package com.example.persistence;

import com.example.persistence.batch.BatchInsertRepository;
import com.example.persistence.entity.TestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


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
}