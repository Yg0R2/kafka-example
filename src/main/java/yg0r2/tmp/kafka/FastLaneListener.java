package yg0r2.tmp.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class FastLaneListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastLaneListener.class);

    @Autowired
    private RequestProcessor requestProcessor;
    @Autowired
    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    @KafkaListener(id = "fastLaneListener", containerFactory = "kafkaFastLaneContainerFactory", topics = "${kafka.fastLane.topic}")
    public void receive(String payload, Acknowledgment acknowledgment) {
        LOGGER.info("received payload='{}'", payload);

        try {
            requestProcessor.handleRequest(payload);
        } catch (Throwable throwable) {
            LOGGER.error("resubmit slowLane");

            slowLaneResubmitProcessor.resubmit(payload);
        }

        LOGGER.info("Acknowledgment");

        acknowledgment.acknowledge();
    }

}
