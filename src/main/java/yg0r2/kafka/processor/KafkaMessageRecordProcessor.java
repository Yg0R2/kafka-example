package yg0r2.kafka.processor;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class KafkaMessageRecordProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageRecordProcessor.class);

    public void processRecord(ConsumerRecord<String, String> record) {
        LOGGER.info("Consumed record: {}", record);
    }

}
