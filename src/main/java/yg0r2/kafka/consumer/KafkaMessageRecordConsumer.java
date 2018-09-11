package yg0r2.kafka.consumer;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.processor.KafkaMessageRecordProcessor;

public class KafkaMessageRecordConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaMessageRecordConsumer.class);

    private final Consumer<RequestCorrelationId, KafkaMessageRecord> kafkaConsumer;
    private final KafkaMessageRecordProcessor kafkaMessageRecordProcessor;
    private final String topic;
    private final long pollTimeout;

    public KafkaMessageRecordConsumer(Consumer<RequestCorrelationId, KafkaMessageRecord> kafkaConsumer,
        KafkaMessageRecordProcessor kafkaMessageRecordProcessor, String topic, long pollTimeout) {

        this.kafkaConsumer = kafkaConsumer;
        this.kafkaMessageRecordProcessor = kafkaMessageRecordProcessor;
        this.topic = topic;
        this.pollTimeout = pollTimeout;
    }

    public void poll() {
        ConsumerRecords<RequestCorrelationId, KafkaMessageRecord> consumerRecords = kafkaConsumer.poll(pollTimeout);

        if (!consumerRecords.isEmpty()) {
            consumerRecords.records(topic)
                .forEach(kafkaMessageRecordProcessor::processRecord);

            kafkaConsumer.commitSync();
        }

        try {
            LOGGER.info("{} - Commit sync records: {}", Thread.currentThread().getName(), new ObjectMapper().writeValueAsString(consumerRecords));
        }
        catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage(), e);
        }

//        kafkaConsumer.commitSync();
    }

}
