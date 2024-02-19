package com.example.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
