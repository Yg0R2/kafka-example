package yg0r2.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;

@Component
public class DefaultSlowLaneBookingEmailRequestSubmitter {

    @Value("${kafka.slowLane.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, KafkaMessageRecord> kafkaTemplate;

    public void submitEmailRequest(KafkaMessageRecord kafkaMessageRecord) {
        kafkaTemplate.send(topic, kafkaMessageRecord);
    }

}
