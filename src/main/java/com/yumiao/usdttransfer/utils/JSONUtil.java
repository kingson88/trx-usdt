package com.yumiao.usdttransfer.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

public class JSONUtil {
    // http://stackoverflow.com/questions/3907929/should-i-make-jacksons-objectmapper-as-static-final
    static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.setSerializationInclusion(Include.NON_NULL);
    }

    public static String toJSON(Object obj) throws JsonGenerationException,
            JsonMappingException, IOException {
        return mapper.writeValueAsString(obj);
    }

    public static String toJSONOrNull(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonNode fromJSON(String json)
            throws JsonProcessingException, IOException {
        return mapper.readTree(json);
    }

    public static <T> T fromJSON(final TypeReference<T> type,
                                 final String jsonPacket) throws JsonParseException,
            JsonMappingException, IOException {
        T data = null;

        data = new ObjectMapper().readValue(jsonPacket, type);
        return data;
    }

    public static <T> T fromJSON(Class<T> type, final String jsonPacket)
            throws JsonParseException, JsonMappingException, IOException {
        T data = null;

        data = new ObjectMapper().readValue(jsonPacket, type);
        return data;
    }

    public static <T> T getFieldValue(JsonNode node, String field,
                                      Function<JsonNode, T> func, T def) {
        JsonNode fieldNode = node.get(field);
        if (fieldNode == null)
            return def;
        T ret = func.apply(fieldNode);
        return ret == null ? def : ret;
    }

    public static <T> T getFieldValue(JsonNode node, String field,
                                      Function<JsonNode, T> func) {
        return getFieldValue(node, field, func, null);
    }

    public static String getTextFieldValue(JsonNode node, String field,
                                           String def) {
        return getFieldValue(node, field, JsonNode::asText, def);
    }

    public static Integer getIntFieldValue(JsonNode node, String field,
                                           Integer def) {
        // 用asInt无法获得NULl结果
        String val = getTextFieldValue(node, field, null);
        if (val == null || val.trim().equalsIgnoreCase(""))
            return def;
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
        }
        return def;
    }

    public static <T> T convertValue(Map<String, ?> map, Class<T> cls) {
        return mapper.convertValue(map, cls);
    }
}
