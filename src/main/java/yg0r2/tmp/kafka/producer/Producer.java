package yg0r2.tmp.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String payload) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, payload);

        send(producerRecord);
    }

    public void send(ProducerRecord<String, String> producerRecord) {
        LOGGER.info("Sending payload='{}' to topic='{}'", producerRecord.value(), producerRecord.topic());

        kafkaTemplate.send(producerRecord);
    }

}
