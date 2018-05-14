package yg0r2.tmp.kafka;

import java.util.List;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

public class BookingEmailRequestConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailRequestConsumer.class);

    private final BookingEmailRequestProcessor bookingEmailRequestProcessor;
    private final Consumer<String, String> kafkaConsumer;
    private final String topic;
    private final long pollTimeout;

    public BookingEmailRequestConsumer(BookingEmailRequestProcessor bookingEmailRequestProcessor, Consumer<String, String> kafkaConsumer, String topic, long pollTimeout) {
        this.bookingEmailRequestProcessor = bookingEmailRequestProcessor;
        this.kafkaConsumer = kafkaConsumer;
        this.topic = topic;
        this.pollTimeout = pollTimeout;
    }

    public void poll() {
        LOGGER.info("Poooolllll... from {}, thread: {}", topic, Thread.currentThread().getThreadGroup().activeCount());

        List<ConsumerRecord<String, String>> records = pollRecords();

        if (!records.isEmpty()) {
            bookingEmailRequestProcessor.processRecords(records);

            kafkaConsumer.commitSync();
        }
    }

    private List<ConsumerRecord<String, String>> pollRecords() {
        ConsumerRecords<String, String> consumerRecords = kafkaConsumer.poll(pollTimeout);

        return new ImmutableList.Builder<ConsumerRecord<String, String>>()
                .addAll(consumerRecords.records(topic))
                .build();
    }


}
