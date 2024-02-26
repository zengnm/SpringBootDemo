package com.example.service.strategy;

import org.springframework.stereotype.Service;

/**
 * @author zengnianmei
 */
@Service
public class Hi implements Say {
    @Override
    public String id() {
        return "hi";
    }

    @Override
    public void say() {
        System.out.println(id());
    }
}
