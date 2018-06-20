package yg0r2.kafka.serialization;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import yg0r2.kafka.domain.KafkaMessageRecord;

public class ModifiedStringDeserializer implements Deserializer<KafkaMessageRecord> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaMessageRecord deserialize(String topic, byte[] data) {
        return new KafkaMessageRecord.Builder()
            .withPayload(readValue(data))
            .build();
    }

    @Override
    public void close() {
    }

    private String readValue(byte[] data) {
        return new String(data);
    }

}
