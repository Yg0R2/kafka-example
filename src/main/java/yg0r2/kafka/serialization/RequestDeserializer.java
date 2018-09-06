package yg0r2.kafka.serialization;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.Request;

public class RequestDeserializer implements Deserializer<KafkaMessageRecord> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestDeserializer.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaMessageRecord deserialize(String topic, byte[] data) {
        return new KafkaMessageRecord.Builder()
            .withRequest(readValue(data))
            .build();
    }

    @Override
    public void close() {
    }

    private Request readValue(byte[] data) {
        Request request;

        try {
            request = objectMapper.readValue(data, Request.class);
        }
        catch (IOException exception) {
            LOGGER.error(String.format("Cannot deserialize given data: %s.", new String(data, Charset.forName("UTF-8"))), exception);

            request = Request.EMPTY_OBJECT;
        }

        return request;
    }

}
