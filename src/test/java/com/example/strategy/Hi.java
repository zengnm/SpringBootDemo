package com.example.strategy;

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
    public String say(Param param) {
        return "hi " + param.getName();
    }
}