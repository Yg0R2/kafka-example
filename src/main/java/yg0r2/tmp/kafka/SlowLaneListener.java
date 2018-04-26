package yg0r2.tmp.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.MessageHeaders;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@Component
public class SlowLaneListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(SlowLaneListener.class);

    @Autowired
    private RequestProcessor requestProcessor;

    @KafkaListener(id = "slowLaneListener", containerFactory = "kafkaSlowLaneContainerFactory", topics = "${kafka.slowLane.topic}")
    //@Retryable(backoff = @Backoff(delay = 3000))
    public void receive(String payload, Acknowledgment acknowledgment) {
        LOGGER.info("received payload='{}'", payload);

        acknowledgment.acknowledge();

        LOGGER.info("Acknowledgment");

        requestProcessor.handleRequest(payload);
    }

}
