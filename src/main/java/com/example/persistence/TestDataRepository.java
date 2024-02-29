package com.example.persistence;

import com.example.persistence.entity.TestData;
import com.example.persistence.entity.TestDataProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TestDataRepository extends JpaRepository<TestData, Long>, JpaSpecificationExecutor<TestData> {

    @Query(name = "queryDataProperty", nativeQuery = true)
    List<TestDataProperty> queryDataProperty(Long id);
}