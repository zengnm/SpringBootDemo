package com.example.controller;

import com.example.persistence.TestDataRepository;
import com.example.persistence.entity.TestData;
import com.example.persistence.entity.TestDataProperty;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.Random;


/**
 * @author zengnianmei
 */
@RestController
public class TestController {

    @GetMapping("/test")
    public String test() throws InterruptedException {
        int sleep = new Random().nextInt(10, 20);
        Thread.sleep(sleep);
        return "success";
    }

    @Resource
    private TestDataRepository testDataRepository;
    @GetMapping("/testData")
    public TestData testData() {
        Optional<TestData> testData = testDataRepository.findById(1L);
        return testData.get();
    }

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/namedQueryByEntityManager")
    public List<TestDataProperty> namedQueryByEntityManager() {
        return entityManager.createNamedQuery("queryDataProperty", TestDataProperty.class)
                .setParameter("id", 1L)
                .getResultList();
    }

    @GetMapping("/namedQuery")
    public List<TestDataProperty> namedQuery() {
        return testDataRepository.queryDataProperty(1L);
    }
}
