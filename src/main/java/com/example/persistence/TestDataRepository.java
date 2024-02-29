package com.example.persistence;

import com.example.persistence.entity.TestData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestDataRepository extends JpaRepository<TestData, Long>, JpaSpecificationExecutor<TestData> {


}