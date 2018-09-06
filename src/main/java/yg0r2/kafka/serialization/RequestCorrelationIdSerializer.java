package yg0r2.kafka.serialization;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import yg0r2.kafka.domain.RequestCorrelationId;

public class RequestCorrelationIdSerializer implements Serializer<RequestCorrelationId> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, RequestCorrelationId data) {
        return writeValueAsBytes(data);
    }

    @Override
    public void close() {
    }

    private byte[] writeValueAsBytes(RequestCorrelationId data) {
        try {
            return (data.getTimestamp() + ":" + data.getRequestId()).getBytes("UTF-8");
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new RequestCorrelationIdSerializationException(unsupportedEncodingException);
        }
    }

}
