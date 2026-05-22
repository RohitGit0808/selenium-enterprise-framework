package com.automation.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class JsonUtils {

    private static final Logger log = LogManager.getLogger(JsonUtils.class);
    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private JsonUtils() {}

    public static <T> T readFromFile(String filePath, Class<T> clazz) {
        try {
            File file = new File(filePath);
            T data = mapper.readValue(file, clazz);
            log.info("JSON data loaded from: {}", filePath);
            return data;
        } catch (IOException e) {
            log.error("Failed to read JSON from file {}: {}", filePath, e.getMessage());
            throw new RuntimeException("Cannot read JSON file: " + filePath, e);
        }
    }

    public static <T> T readFromFile(String filePath, TypeReference<T> typeRef) {
        try {
            T data = mapper.readValue(new File(filePath), typeRef);
            log.info("JSON data loaded (generic) from: {}", filePath);
            return data;
        } catch (IOException e) {
            log.error("Failed to read JSON: {}", e.getMessage());
            throw new RuntimeException("Cannot read JSON file: " + filePath, e);
        }
    }

    public static <T> T readFromResource(String resourcePath, Class<T> clazz) {
        try (InputStream is = JsonUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (is == null) throw new RuntimeException("Resource not found: " + resourcePath);
            return mapper.readValue(is, clazz);
        } catch (IOException e) {
            log.error("Failed to read JSON resource {}: {}", resourcePath, e.getMessage());
            throw new RuntimeException("Cannot read JSON resource: " + resourcePath, e);
        }
    }

    public static JsonNode parseJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException("Cannot parse JSON string", e);
        }
    }

    public static Map<String, Object> toMap(String filePath) {
        return readFromFile(filePath, new TypeReference<>() {});
    }

    public static List<Map<String, Object>> toListOfMaps(String filePath) {
        return readFromFile(filePath, new TypeReference<>() {});
    }

    public static String getValueFromJson(String filePath, String key) {
        Map<String, Object> data = toMap(filePath);
        Object value = data.get(key);
        return value != null ? value.toString() : null;
    }

    public static String toJsonString(Object obj) {
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException("Cannot serialize to JSON", e);
        }
    }
}
