package com.ederson.beerstock.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonConvertionUtils {
    public JsonConvertionUtils() {
    }

    public static String asJsonString(Object bookDTO) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            objectMapper.registerModules(new Module[]{new JavaTimeModule()});
            return objectMapper.writeValueAsString(bookDTO);
        } catch (Exception var2) {
            throw new RuntimeException(var2);
        }
    }
}
