package com.example.controller;

import com.example.persistence.TestDataPropertyRepository;
import com.example.persistence.TestDataRepository;
import com.example.persistence.entity.TestData;
import com.example.persistence.entity.TestDataProperty;
import jakarta.annotation.Resource;
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

    @Resource
    private TestDataPropertyRepository testDataPropertyRepository;
    @GetMapping("/namedQuery")
    public List<TestDataProperty> namedQuery() {
        return testDataPropertyRepository.queryDataProperty(1L);
    }
}
