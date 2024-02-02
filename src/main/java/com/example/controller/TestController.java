package com.example.controller;

import com.example.repository.Test;
import com.example.repository.TestRepository;
import jakarta.annotation.Resource;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
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

    @PostMapping(value = "/getAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Test> all(@RequestBody Test test) {
        ExampleMatcher matching = ExampleMatcher.matching()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.startsWith());
        return testRepository.findAll(Example.of(test, matching));
    }
}
