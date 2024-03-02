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

    @Override
    protected void initInsertColumnProperty(BaseRepository<TestData, Long>.InsertColumnProperties ips) {
        ips.table("test_data")
                .addInsertColumn("name", TestData::getName)
                .addInsertColumn("status", TestData::getStatus);
    }
}