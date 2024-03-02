package com.example.persistence;

import com.example.persistence.entity.TestDataProperty;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@SpringBootTest
public class JpaTest {
    @Resource
    private TestDataPropertyRepository testDataPropertyRepository;

    @Test
    public void testDynamicPage() {
        Pageable pageable = Pageable.ofSize(2);
        Page<TestDataProperty> page = testDataPropertyRepository.queryPage(List.of(1L), null, pageable);
        Assertions.assertTrue(page.getContent().size() <= 2);
    }

    @Test
    public void testDynamicPageSort() {
        Pageable pageable = PageRequest.of(1, 2, Sort.by("id").descending());
        Page<TestDataProperty> page = testDataPropertyRepository.queryPage(null, null, pageable);
        if (!page.getContent().isEmpty()) {
            Assertions.assertTrue(page.getContent().getFirst().getId() >= page.getContent().getLast().getId());
        }
    }
}
