package com.example.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    private final static Logger LOG = LoggerFactory.getLogger(JSONUtil.class);
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
            LOG.error("toJSONString error:", e);
        }
        return jsonStr;
    }

    public static <T> T parseObject(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (IOException e) {
            LOG.error("parseObject exception, json=" + json , e);
        }
        return null;
    }

    public static <T> List<T> parseList(String jsonArray, Class<T> clazz){
        if (null == jsonArray || jsonArray.isEmpty() || null == clazz) {
            return new ArrayList<>();
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
            return objectMapper.readValue(jsonArray, javaType);
        } catch (IOException e) {
            LOG.error("parseList exception, jsonArray=" + jsonArray, e);
        }
        return new ArrayList<>();
    }

    public static <T> Map<String, T> parseMap(String json, Class<T> clazz) {
        if (null == json || json.isEmpty() || null == clazz) {
            return new HashMap<>();
        }
        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(HashMap.class,String.class, clazz);
            return objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            LOG.error("parseMap exception, json=" + json, e);
        }
        return new HashMap<>();
    }

    public static Map<String, Object> parseMap(String json) {
        return parseMap(json, Object.class);
    }

    public static List<Map<String, Object>> parseListMap(String json) {
        if (null == json || json.isEmpty()) {
            return new ArrayList<>();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, Object>>>(){});
        } catch (IOException e) {
            LOG.error("parseListMap exception, json=" + json, e);
        }
        return new ArrayList<>();
    }
}
