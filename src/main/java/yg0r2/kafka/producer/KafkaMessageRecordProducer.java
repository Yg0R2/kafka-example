package yg0r2.kafka.producer;

import yg0r2.kafka.domain.KafkaMessageRecord;

public interface KafkaMessageRecordProducer {

    void submitRequest(KafkaMessageRecord kafkaMessageRecord);
}
