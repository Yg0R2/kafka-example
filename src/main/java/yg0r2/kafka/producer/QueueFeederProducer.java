package yg0r2.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.google.common.annotations.VisibleForTesting;

@Component
@VisibleForTesting
public class QueueFeederProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueueFeederProducer.class);

    @Autowired
    @Qualifier("queueFeederKafkaTemplate")
    private KafkaTemplate<String, String> queueFeederKafkaTemplate;

    public void send(String topic, String payload) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topic, payload);

        LOGGER.info("Sending payload='{}' to topic='{}'", payload, producerRecord.topic());

        queueFeederKafkaTemplate.send(producerRecord);
    }

}
