package com.example.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



/**
 * @author zengnianmei
 */
@RestController
public class TestController {

    @RequestMapping("/test")
    public String test() throws InterruptedException {
        return "success".repeat(1000);
    }
}
