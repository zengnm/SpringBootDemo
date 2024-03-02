package com.example.persistence;

import com.example.persistence.batch.BaseRepository;
import com.example.persistence.entity.TestData;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

@Repository
public class TestDataRepository extends BaseRepository<TestData, Long> {
    public TestDataRepository(EntityManager entityManager) {
        super(TestData.class, entityManager);
    }
}