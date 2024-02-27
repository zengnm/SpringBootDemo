package com.example.strategy;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author zengnianmei
 */
@SpringBootTest
public class SelectorTest {
    @Resource
    private Say say;

    @Test
    public void test() {
        Param param = new Param();
        param.setType("hi");
        param.setName("java");
        Assertions.assertEquals(("hi "+param.getName()), say.say(param));
        param.setType("hello");
        Assertions.assertEquals(("hello "+param.getName()), say.say(param));
    }
}
