package org.civis.blockchain.ssm.client.Utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CompletionException;
import java.util.function.Function;

public class JsonUtils {

    private static final ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
    }

    public static String toJson(Object obj) throws JsonProcessingException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper.writeValueAsString(obj);
    }

    public static <T> T toObject(String value, Class<T> clazz) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper.readValue(value, clazz);
    }

    public static <T> T toObject(String value, TypeReference<T> clazz) throws IOException {
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        return mapper.readValue(value, clazz);
    }

}
