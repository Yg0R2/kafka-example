package yg0r2.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;

@Component
public class SlowLaneKafkaMessageRecordProducer implements KafkaMessageRecordProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlowLaneKafkaMessageRecordProducer.class);

    @Value("${kafka.slowLane.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<RequestCorrelationId, KafkaMessageRecord> slowLaneKafkaTemplate;

    @Override
    public void submitRequest(KafkaMessageRecord kafkaMessageRecord) {
        slowLaneKafkaTemplate.send(topic, createRequestCorrelationId(kafkaMessageRecord), kafkaMessageRecord);

        LOGGER.info("Submit request: {} to topic: {}", kafkaMessageRecord, topic);
    }

    private RequestCorrelationId createRequestCorrelationId(KafkaMessageRecord kafkaMessageRecord) {
        return new RequestCorrelationId(kafkaMessageRecord.getRequestId(), kafkaMessageRecord.getTimestamp());
    }

}
