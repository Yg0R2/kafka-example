package yg0r2.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.processor.KafkaMessageRecordProcessor;

@Component
class DefaultKafkaMessageRecordConsumer implements KafkaMessageRecordConsumer {

    @Value("${kafka.pollTimeout}")
    private long pollTimeout;

    @Autowired
    private Consumer<RequestCorrelationId, KafkaMessageRecord> kafkaConsumer;
    @Autowired
    private KafkaMessageRecordProcessor kafkaMessageRecordProcessor;

    @Override
    public void poll() {
        ConsumerRecords<RequestCorrelationId, KafkaMessageRecord> records = kafkaConsumer.poll(pollTimeout);

        records.forEach(kafkaMessageRecordProcessor::processRecord);
    }

}
