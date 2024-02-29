package com.example.persistence;

import com.example.persistence.entity.TestProperty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TestPropertyRepository extends JpaRepository<TestProperty, Long>, JpaSpecificationExecutor<TestProperty> {

}