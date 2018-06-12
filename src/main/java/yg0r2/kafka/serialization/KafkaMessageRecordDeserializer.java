package yg0r2.kafka.serialization;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import yg0r2.kafka.domain.KafkaMessageRecord;

public class KafkaMessageRecordDeserializer implements Deserializer<KafkaMessageRecord<String>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageRecordDeserializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());;

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaMessageRecord<String> deserialize(String topic, byte[] data) {
        return readValue(data);
    }

    @Override
    public void close() {
    }

    private KafkaMessageRecord<String> readValue(byte[] data) {
        KafkaMessageRecord<String> kafkaMessageRecord;

        try {
            kafkaMessageRecord = objectMapper.readValue(data, KafkaMessageRecord.class);
        } catch (IOException exception) {
            LOGGER.error(String.format("Cannot deserialize given data: %s.", new String(data, Charset.forName("UTF-8"))), exception);

            kafkaMessageRecord = KafkaMessageRecord.NULL_OBJECT;
        }

        return kafkaMessageRecord;
    }

}
