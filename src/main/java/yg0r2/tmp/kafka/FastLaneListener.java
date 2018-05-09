package yg0r2.tmp.kafka;

import java.util.List;

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
    public void receive(List<ConsumerRecord<String, String>> records, Acknowledgment acknowledgment) {
        records.stream().forEach(this::processRecord);

        LOGGER.info("Acknowledgment");
        acknowledgment.acknowledge();
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        LOGGER.info("Record consumed from topic={} partition={} offset={} payload={}",
            record.topic(), record.partition(), record.offset(), record.value());

        requestProcessor.handleRequest(record.value());
    }

}
