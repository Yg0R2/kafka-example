package yg0r2.kafka.consumer;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.service.BookingEmailRequestRecordProcessor;

public class BookingEmailRequestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailRequestConsumer.class);

    private final BookingEmailRequestRecordProcessor bookingEmailRequestRecordProcessor;
    private final Consumer<String, KafkaMessageRecord<String>> kafkaConsumer;
    private final String topic;
    private final long pollTimeout;

    public BookingEmailRequestConsumer(BookingEmailRequestRecordProcessor bookingEmailRequestRecordProcessor, Consumer<String, KafkaMessageRecord<String>> kafkaConsumer, String topic, long pollTimeout) {
        this.bookingEmailRequestRecordProcessor = bookingEmailRequestRecordProcessor;
        this.kafkaConsumer = kafkaConsumer;
        this.topic = topic;
        this.pollTimeout = pollTimeout;
    }

    public void poll() {
        List<ConsumerRecord<String, KafkaMessageRecord<String>>> records = pollRecords();

        if (!records.isEmpty()) {
            bookingEmailRequestRecordProcessor.processRecords(records);

            kafkaConsumer.commitSync();
        }
    }

    private List<ConsumerRecord<String, KafkaMessageRecord<String>>> pollRecords() {
        LOGGER.info("Polled from: " + Thread.currentThread().getName());

        ConsumerRecords<String, KafkaMessageRecord<String>> consumerRecords = kafkaConsumer.poll(pollTimeout);

        return new ImmutableList.Builder<ConsumerRecord<String, KafkaMessageRecord<String>>>()
            .addAll(consumerRecords.records(topic))
            .build();
    }

}