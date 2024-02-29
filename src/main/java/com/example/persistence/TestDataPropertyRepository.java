package com.example.persistence;

import com.example.persistence.entity.TestDataProperty;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface TestDataPropertyRepository extends Repository<TestDataProperty, Long> {
    @Query(value = """
            select d.id, d.name, d.status, p.content,  d.create_time, d.update_time
            from test_data d left join test_property p on d.name = p.name
            where d.id > :id
            """, nativeQuery = true)
    List<TestDataProperty> queryDataProperty(Long id);
}
