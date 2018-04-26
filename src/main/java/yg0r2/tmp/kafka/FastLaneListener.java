package yg0r2.tmp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
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

    @KafkaListener(id = "fastLaneListener", containerFactory = "kafkaFastLaneContainerFactory", topics = "${kafka.fastLane.topic}")
    public void receive(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        LOGGER.info("Record consumed from topic={} partition={} offset={}", record.topic(), record.partition(), record.offset());

        requestProcessor.handleRequest(record.value());

        LOGGER.info("Acknowledgment");
        acknowledgment.acknowledge();
    }

}
