package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Random;

/**
 * @author zengnianmei
 */
@RestController
public class TestController {
    @GetMapping("/test")
    public Mono<String> test() {
        int delay = new Random().nextInt(10, 20);
        return Mono.delay(Duration.ofMillis(delay)).map(e -> "success");
    }
}
