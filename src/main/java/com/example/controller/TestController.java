package com.example.controller;

import com.example.repository.Test;
import com.example.repository.TestRepository;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

/**
 * @author zengnianmei
 */
@RestController
public class TestController {
    @Resource
    private TestRepository testRepository;

    @GetMapping("/test")
    public Mono<String> test() {
        int delay = new Random().nextInt(10, 20);
        return Mono.delay(Duration.ofMillis(delay)).map(e -> "success");
    }

    @GetMapping("/getById")
    public Mono<Test> getById(@RequestParam Long id) {
        return testRepository.findById(id);
    }
}
