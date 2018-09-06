package yg0r2.kafka.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
class DefaultKafkaMessageRecordProducer implements KafkaMessageRecordProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultKafkaMessageRecordProducer.class);

    @Value("${kafka.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void submitRequest(String data) {
        kafkaTemplate.send(topic, data);

        LOGGER.info("Submit request: {} to topic: {}", data, topic);
    }

}
