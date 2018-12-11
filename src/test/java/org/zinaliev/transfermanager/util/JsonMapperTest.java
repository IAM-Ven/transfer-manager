package org.zinaliev.transfermanager.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.zinaliev.transfermanager.exception.JsonReadException;
import org.zinaliev.transfermanager.exception.JsonWriteException;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class JsonMapperTest {

    private final ObjectMapper jackson = mock(ObjectMapper.class);

    private final JsonMapper mapper = new JsonMapper(jackson);

    private final Object object = new Object();
    private final String json = "";

    @Test
    public void testToJson_ValidInput_ReturnsJsonProducedByJackson() throws JsonProcessingException {
        when(jackson.writeValueAsString(object)).thenReturn(json);

        assertEquals(json, mapper.toJson(object));
        verify(jackson).writeValueAsString(object);
    }

    @Test(expected = JsonWriteException.class)
    public void testToJson_InvalidInput_ThrowsUncheckedException() throws JsonProcessingException {
        when(jackson.writeValueAsString(object)).thenThrow(mock(JsonProcessingException.class));

        mapper.toJson(object);
    }

    @Test
    public void testFromJson_ValidInput_ReturnsObjectProducedByJackson() throws IOException {
        when(jackson.readValue(json, Object.class)).thenReturn(object);

        assertEquals(object, mapper.fromJson(json, Object.class));
        verify(jackson).readValue(json, Object.class);
    }

    @Test(expected = JsonReadException.class)
    public void testFromJson_InvalidInput_ThrowsUncheckedException() throws IOException {
        when(jackson.readValue(json, Object.class)).thenThrow(mock(IOException.class));

        mapper.fromJson(json, Object.class);
    }
}