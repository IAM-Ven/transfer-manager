package org.zinaliev.transfermanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import unirest.ObjectMapper;
import unirest.Unirest;

import java.io.IOException;

public class UnirestUtils {

    private UnirestUtils() {
    }

    public static void setupSerializer() {
        // Only one time
        Unirest.config().setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
