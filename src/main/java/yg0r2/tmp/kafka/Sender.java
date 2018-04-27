package yg0r2.tmp.kafka;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
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
        LOGGER.info("sending payload='{}' to topic='{}'", payload, topic);

        long timestamp = Timestamp.valueOf(LocalDateTime.now().plusSeconds(5)).getTime();
        List<Header> headers = new ArrayList<>();

        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, null, timestamp, null, payload, headers);

        kafkaTemplate.send(producerRecord);
    }

}
