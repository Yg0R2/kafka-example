package yg0r2.kafka.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import yg0r2.kafka.domain.KafkaMessageRecord;

public class KafkaMessageRecordSerializer implements Serializer<KafkaMessageRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageRecordSerializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, KafkaMessageRecord data) {
        return writeValue(data);
    }

    @Override
    public void close() {
    }

    private byte[] writeValue(KafkaMessageRecord kafkaMessageRecord) {
        try {
            return objectMapper.writeValueAsBytes(kafkaMessageRecord);
        }
        catch (JsonProcessingException e) {
            LOGGER.error("Unable to serialize message record.");

            throw new RuntimeException(e);
        }
    }
}
