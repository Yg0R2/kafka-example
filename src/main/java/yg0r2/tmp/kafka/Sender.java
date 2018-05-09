package yg0r2.tmp.kafka;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Collections;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.utils.SystemTime;
import org.apache.kafka.common.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String payload) {
        long timestamp = Instant.now().plusSeconds(5).toEpochMilli();

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null, timestamp, null, payload, Collections.EMPTY_LIST);

        kafkaTemplate.send(producerRecord);
    }

    public void send(ProducerRecord<String, String> producerRecord) {
        LOGGER.info("Sending payload='{}' to topic='{}' with timestamp='{}'", producerRecord.value(), producerRecord.topic(), producerRecord.timestamp());

        kafkaTemplate.send(producerRecord);
    }

}
