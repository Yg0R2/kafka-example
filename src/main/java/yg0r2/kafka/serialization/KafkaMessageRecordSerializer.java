package yg0r2.kafka.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import yg0r2.kafka.domain.KafkaMessageRecord;

public class KafkaMessageRecordSerializer implements Serializer<KafkaMessageRecord> {

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, KafkaMessageRecord data) {
        return writeValueAsBytes(data);
    }

    @Override
    public void close() {
    }

    private byte[] writeValueAsBytes(KafkaMessageRecord data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        }
        catch (JsonProcessingException e) {
            throw new KafkaMessageRecordSerializationException(e);
        }
    }

}
