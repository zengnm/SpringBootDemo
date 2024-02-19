package com.example;

import com.example.infrastructure.persistence.entity.Test;
import com.example.infrastructure.persistence.TestRepository;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

/**
 * @author zengnianmei
 */
@SpringBootTest
public class JpaTest {
    @Resource
    private TestRepository testRepository;

    @org.junit.jupiter.api.Test
    public void findById() {
        Optional<Test> test = testRepository.findById(1L);
        test.ifPresent(value -> Assertions.assertEquals(value.getId(), 1L));
    }

    @org.junit.jupiter.api.Test
    public void findByExamplePageable() {
        String nameStartWith = "Chan Chi";
        Test probe = new Test();
        probe.setName(nameStartWith);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith());
        int pageSize = 10;

        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "id"));
        Page<Test> page = testRepository.findAll(Example.of(probe, matcher), pageable);
        Assertions.assertEquals(page.getSize(), pageSize);
        if (page.getSize() > 0) {
            Assertions.assertTrue(page.get().allMatch(e -> e.getName().startsWith(nameStartWith)));
        }
        if (page.getSize() > 2) {
            List<Test> content = page.getContent();
            Assertions.assertTrue(content.get(0).getId() > content.get(1).getId());
        }
    }

    @org.junit.jupiter.api.Test
    public void query() {
        String nameMatch = "Chan Chi";
        List<Test> tests = testRepository.querySql(nameMatch+"%");
        if (!tests.isEmpty()) {
            Assertions.assertTrue(tests.stream().allMatch(e -> e.getName().contains(nameMatch)));
        }
    }
}
