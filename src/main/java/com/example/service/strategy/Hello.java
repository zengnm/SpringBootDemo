package com.example.service.strategy;

import org.springframework.stereotype.Service;

/**
 * @author zengnianmei
 */
@Service
public class Hello implements Say{
    @Override
    public String id() {
        return "hello";
    }

    @Override
    public void say() {
        System.out.println(id());
    }
}
