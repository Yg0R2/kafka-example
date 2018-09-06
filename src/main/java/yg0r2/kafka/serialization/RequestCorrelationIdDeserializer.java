package yg0r2.kafka.serialization;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.UUID;

import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.domain.RequestCorrelationId;

public class RequestCorrelationIdDeserializer implements Deserializer<RequestCorrelationId> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestCorrelationIdDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public RequestCorrelationId deserialize(String topic, byte[] data) {
        return readValue(data);
    }

    @Override
    public void close() {
    }

    private RequestCorrelationId readValue(byte[] data) {
        RequestCorrelationId requestCorrelationId;

        try {
            String[] keyFragments = new String(data, "UTF-8").split(":");
            requestCorrelationId = new RequestCorrelationId(UUID.fromString(keyFragments[1]), Long.parseLong(keyFragments[0]));
        }
        catch (Exception exception) {
            LOGGER.error(String.format("Cannot deserialize given data: %s.", new String(data, Charset.forName("UTF-8"))), exception);

            requestCorrelationId = RequestCorrelationId.EMPTY_OBJECT;
        }

        return requestCorrelationId;
    }

}
