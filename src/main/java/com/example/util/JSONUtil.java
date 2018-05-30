package com.example.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JSONUtil {
    private static final Logger LOGGER = LogManager.getLogger(JSONUtil.class);
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
        String jsonStr = "";
        try {
            jsonStr = objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            LOGGER.error("toJSONString error:", e);
        }
        return jsonStr;
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LOGGER.error("parseObject exception, json=" + json , e);
        }
        return null;
    }

    public static <T> List<T> parseList(String jsonArray, Class<T> clazz){
        if (null == jsonArray || jsonArray.length() == 0 || null == clazz) {
            return new ArrayList<>();
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(jsonArray, javaType);
        } catch (IOException e) {
            LOGGER.error("parseList exception, jsonArray=" + jsonArray, e);
        }
        return new ArrayList<>();
    }

    public static <T> Map<String, T> parseMap(String json, Class<T> clazz) {
        if (null == json || json.length() == 0 || null == clazz) {
            return new HashMap<>();
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(HashMap.class,String.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            LOGGER.error("parseMap exception, json=" + json, e);
        }
        return new HashMap<>();
    }

    public static Map<String, Object> parseMap(String json) {
        return parseMap(json, Object.class);
    }

    public static List<Map<String, Object>> parseListMap(String json) {
        if (null == json || json.length() == 0) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>(){});
        } catch (IOException e) {
            LOGGER.error("parseListMap exception, json=" + json, e);
        }
        return new ArrayList<>();
    }
}
