package com.example.persistence;

import com.example.persistence.entity.TestData;
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
import java.util.Optional;

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

    @Resource
    private TestDataRepository testDataRepository;
    @Test
    public void test() {
        Optional<TestData> testData = testDataRepository.queryById(1L);
        testData.ifPresent(data -> Assertions.assertEquals(1L, (long) data.getId()));
    }
}
