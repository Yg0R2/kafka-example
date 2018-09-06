package yg0r2.kafka.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import yg0r2.kafka.domain.Request;

public class RequestSerializer implements Serializer<Request> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Request data) {
        return writeValueAsBytes(data);
    }

    @Override
    public void close() {
    }

    private byte[] writeValueAsBytes(Request data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        }
        catch (JsonProcessingException e) {
            throw new RequestSerializationException(e);
        }
    }

}
