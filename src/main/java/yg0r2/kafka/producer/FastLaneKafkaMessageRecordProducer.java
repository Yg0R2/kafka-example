package yg0r2.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.Request;
import yg0r2.kafka.domain.RequestCorrelationId;

@Component
public class FastLaneKafkaMessageRecordProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastLaneKafkaMessageRecordProducer.class);

    @Value("${kafka.fastLane.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<RequestCorrelationId, Request> fastLaneKafkaTemplate;

    public void submitRequest(Request request) {
        fastLaneKafkaTemplate.send(topic, createRequestCorrelationId(request), request);

        LOGGER.info("Submit request: {} to topic: {}", request, topic);
    }

    private RequestCorrelationId createRequestCorrelationId(Request request) {
        return new RequestCorrelationId(request.getRequestId(), request.getTimestamp());
    }

}
