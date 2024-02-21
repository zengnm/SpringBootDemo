package com.example;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.UUID;

/**
 * @author zengnianmei
 */
@SpringBootTest
public class RedisTest {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Test
    public void testKeyValue() {
        String key = "key1", value = UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(key, value);
        Assertions.assertEquals(stringRedisTemplate.opsForValue().get(key), value);
    }

    record TestRedisData(Long id, String name){}
    @Test
    public void testJsonObject() {
        String key = "key2";
        TestRedisData value = new TestRedisData(101L, "test");
        redisTemplate.opsForValue().set(key, value);
        Assertions.assertEquals(redisTemplate.opsForValue().get(key), value);
    }
}