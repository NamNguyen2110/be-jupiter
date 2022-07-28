package com.jupiter.gateway.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JsonUtils {
    private final ObjectMapper objectMapper;

    public <T> T convertStringToObject(String input, Class<T> tClass) {
        try {
            return this.objectMapper.readValue(input, tClass);
        } catch (Exception ex) {
            return null;
        }
    }

    public <T> String convertObjToString(T clazz) {
        try {
            return this.objectMapper.writeValueAsString(clazz);
        } catch (Exception ex) {
            return null;
        }
    }

    public <T> String convertObjToStringToLog(T clazz) {
        try {
            String string = this.objectMapper.writeValueAsString(clazz);
            return string.length() < 2000 ? string : String.format("%s ... %s", string.substring(0, 1000), string.substring(string.length() - 1000, string.length() - 1));
        } catch (Exception ex) {
            return null;
        }
    }

    public JsonNode fromJsonString(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
