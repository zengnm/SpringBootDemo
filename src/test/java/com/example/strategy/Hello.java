package com.example.strategy;

import org.springframework.stereotype.Service;

/**
 * @author zengnianmei
 */
@Service
public class Hello implements Say {
    @Override
    public String id() {
        return "hello";
    }

    @Override
    public String say(Param param) {
        return "hello " + param.getName();
    }
}
