package yg0r2.tmp.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DefaultSlowLaneBookingEmailRequestSubmitter {

    @Value("${kafka.slowLane.topic}")
    private String topic;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;


    public void submitEmailRequest(String payload) {
        kafkaTemplate.send(topic, payload);
    }

}
