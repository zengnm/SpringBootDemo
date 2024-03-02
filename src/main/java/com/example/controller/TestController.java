package com.example.controller;

import com.example.persistence.TestDataPropertyRepository;
import com.example.persistence.TestDataRepository;
import com.example.persistence.entity.TestData;
import com.example.persistence.entity.TestDataProperty;
import jakarta.annotation.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;


/**
 * @author zengnianmei
 */
@RestController
@Transactional
public class TestController {
    @Resource
    private TestDataRepository testDataRepository;

    @GetMapping("/testData")
    public TestData testData() {
        Optional<TestData> testData = testDataRepository.findById(1L);
        return testData.get();
    }

    @GetMapping("/batchInsert/{type}")
    public String batchInsert(@PathVariable Integer type) {
        int start = (int) (System.currentTimeMillis() % 10000000L);
        List<TestData> list = IntStream.rangeClosed(1, 155).boxed().map(i -> {
            int t = start + i;
            return new TestData().setName(t + "").setStatus(t);
        }).toList();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        if (type == 1) {
            List<TestData> result = testDataRepository.saveAll(list);
            stopWatch.stop();
            return String.format("start:%d, end:%d, mills: %d", result.getFirst().getId(), result.getLast().getId(), stopWatch.getTotalTimeMillis());
        } else {
            int affect = testDataRepository.batchInsert(list);
            stopWatch.stop();
            return String.format("affect:%d, mills:%d", affect, stopWatch.getTotalTimeMillis());
        }
    }

    @Resource
    private TestDataPropertyRepository testDataPropertyRepository;

    @GetMapping("/namedQuery")
    public List<TestDataProperty> namedQuery() {
        return testDataPropertyRepository.queryDataProperty(1L);
    }
}
