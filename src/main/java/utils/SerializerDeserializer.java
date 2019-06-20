package utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class SerializerDeserializer {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static String SerializerToString(Object object) throws JsonProcessingException {
        return OBJECT_MAPPER.writeValueAsString(object);
    }
    private static <T> T DeserializeStringInObject(String str, Class<T> tClass) throws IOException {
        return OBJECT_MAPPER.readValue(str, tClass);
    }
}
