package org.zinaliev.transfermanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.zinaliev.transfermanager.exception.JsonReadException;
import org.zinaliev.transfermanager.exception.JsonWriteException;

import java.io.IOException;

@Singleton
@Slf4j
public class JsonSerializer {

    private final ObjectMapper jackson;

    @Inject
    public JsonSerializer(ObjectMapper jackson) {
        this.jackson = jackson;
    }

    public String toJson(Object object) {
        try {
            return jackson.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new JsonWriteException(e);
        }
    }

    public <T> T fromJson(String json, Class<T> clazz) {
        try {
            return jackson.readValue(json, clazz);
        } catch (IOException e) {
            throw new JsonReadException(e);
        }
    }
}
