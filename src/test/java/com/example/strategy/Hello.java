package com.example.strategy;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zengnianmei
 */
@Service
public class Hello implements Say {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;
    @Override
    public String id() {
        return "hello";
    }

    @Override
    public String say(Param param) {
        Objects.requireNonNull(redisTemplate);
        return "hello " + param.getName();
    }
}
