package com.example.controller;

import com.example.util.RedisUtil;
import com.example.util.WebHelper;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class CacheController {
    @RequestMapping("/cacheView")
    public String cacheView(Model model) {
        model.addAttribute("userName", WebHelper.getPin());
        model.addAttribute("navigate", 1);
        model.addAttribute("subNavigate", "cache");
        return "cache/cacheView";
    }


    /**
     * 读取缓存,如果没有则把返回值进行缓存
     *
     * @Cacheable 参数说明：
     * value  必须的,指明缓存被缓存到什么地方(对应redis中的zset {value}~keys)。
     * key   Spring默认使用被@Cacheable注解的方法的签名来作为key
     * keyGenerator key生成器, 已在RedisConfig中配置
     * condition="#age<25" 数将指明方法的返回结果是否被缓存。
     */
    @RequestMapping("/getTest")
    @ResponseBody
    @Cacheable(value = "test", keyGenerator = "keyGenerator")
    public String getTest() {
        System.out.println("do getTest");
        return "SUCCESS";
    }

    @RequestMapping("/getTest1/{key}")
    @ResponseBody
    @Cacheable(value = "test1", key = "#key")
    public String getTest1(@PathVariable String key) {
        System.out.println("do getTest1");
        return "SUCCESS1";
    }

    @RequestMapping("/get/{key}")
    @ResponseBody
    public String getCache(@PathVariable String key) {
        Object value = RedisUtil.get(key);
        return null == value ? "" : value.toString();
    }

    @RequestMapping("/set/{key}/{value}")
    @ResponseBody
    public String setCache(@PathVariable String key, @PathVariable String value) {
        System.out.println("是否已存在该key：" + RedisUtil.exists(key));
        RedisUtil.set(key, value);
        return key + "=" + RedisUtil.get(key);
    }
}
