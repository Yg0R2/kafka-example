package yg0r2.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.Request;
import yg0r2.kafka.domain.RequestCorrelationId;

@Component
public class SlowLaneKafkaMessageRecordProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlowLaneKafkaMessageRecordProducer.class);

    @Value("${kafka.slowLane.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<RequestCorrelationId, KafkaMessageRecord> slowLaneKafkaTemplate;

    public void submit(KafkaMessageRecord kafkaMessageRecord) {
        slowLaneKafkaTemplate.send(topic, createRequestCorrelationId(kafkaMessageRecord.getRequest()), kafkaMessageRecord);

        LOGGER.info("Submit request: {} to topic: {}", kafkaMessageRecord, topic);
    }

    private RequestCorrelationId createRequestCorrelationId(Request request) {
        return new RequestCorrelationId(request.getRequestId(), request.getTimestamp());
    }

}
