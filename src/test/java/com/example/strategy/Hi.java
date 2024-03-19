package com.example.strategy;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author zengnianmei
 */
@Service
public class Hi implements Say {
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public String id() {
        return "hi";
    }

    @Override
    public String say(Param param) {
        Objects.requireNonNull(redisTemplate);
        return "hi " + param.getName();
    }
}