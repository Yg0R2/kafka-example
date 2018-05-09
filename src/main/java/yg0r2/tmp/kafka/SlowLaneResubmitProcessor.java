package yg0r2.tmp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SlowLaneResubmitProcessor {

    @Value("${kafka.slowLane.topic}")
    private String slowLaneTopic;

    @Autowired
    private Sender sender;

    public void resubmit(String payload) {
        sender.send(slowLaneTopic, payload);
    }

    public void resubmit(ConsumerRecord<?, ?> record) {
        RecordHeaders recordHeaders = new RecordHeaders();

        ProducerRecord producerRecord =
            new ProducerRecord<>(record.topic(), null, record.timestamp(), null, record.value(), recordHeaders);

        sender.send(producerRecord);
    }
}
