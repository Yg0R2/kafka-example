package yg0r2.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;

import yg0r2.kafka.domain.KafkaMessageRecord;

@Component
@VisibleForTesting
public class QueryFeederProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryFeederProducer.class);

    @Autowired
    private KafkaTemplate<String, KafkaMessageRecord<String>> kafkaTemplate;

    public void send(String topic, String payload) {
        ProducerRecord<String, KafkaMessageRecord<String>> producerRecord = new ProducerRecord<>(topic, getKafkaMessageRecord(payload));

        LOGGER.info("Sending payload='{}' to topic='{}'", payload, producerRecord.topic());

        kafkaTemplate.send(producerRecord);
    }

    private KafkaMessageRecord<String> getKafkaMessageRecord(String payload) {
        return new KafkaMessageRecord.Builder<String>()
            .withPayload(payload)
            .build();
    }

}
