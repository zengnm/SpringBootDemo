package com.example.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * json序列化、反序列化工具
 *
 * @author zengnianmei
 */
public class JSONUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 兼容单引号，但单引号不属于json标准不建议使用
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        // 序列化时，为null的属性不加入到json中
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 反序列化时，不存在相匹配属性的兼容处理
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private JSONUtil() {
    }

    public static String toJSONString(Object o) {
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseList(String jsonArray, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(jsonArray, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Map<String, T> parseMap(String json, Class<T> clazz) {
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(HashMap.class, String.class,
                    clazz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Object> parseMap(String json) {
        return parseMap(json, Object.class);
    }

    public static List<Map<String, Object>> parseListMap(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        record Pojo(Long id, String name) {
        }
        Pojo p1 = new Pojo(1L, "111");
        String json = JSONUtil.toJSONString(p1);
        Pojo o1 = JSONUtil.parseObject(json, Pojo.class);
        System.out.println(o1);

        Pojo p2 = new Pojo(2L, "222");
        json = JSONUtil.toJSONString(List.of(p1, p2));
        List<Pojo> pojos = JSONUtil.parseList(json, Pojo.class);
        System.out.println(pojos);

        Map<String, Integer> map = Map.of("key1", 1, "key2", 2);
        json = JSONUtil.toJSONString(map);
        Map<String, Object> stringObjectMap = JSONUtil.parseMap(json);
        System.out.println(stringObjectMap);

        List<Map<String, Integer>> listMap = List.of(map, Map.of("key3", 3));
        json = JSONUtil.toJSONString(listMap);
        List<Map<String, Object>> maps = JSONUtil.parseListMap(json);
        System.out.println(maps);
    }
}
