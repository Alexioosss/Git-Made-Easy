package com.gitmadeeasy.testUtil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static String objectToJson(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch(Exception e) {
            throw new RuntimeException("Could not convert the object to JSON.\n", e);
        }
    }

    /*
    Helper method to extract a given field value from a JSON string
     */
    public static String readJson(String jsonString, String fieldName) {
        try {
            return new ObjectMapper().readTree(jsonString).get(fieldName).asText();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}