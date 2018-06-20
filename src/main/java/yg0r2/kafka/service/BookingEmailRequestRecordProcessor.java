package yg0r2.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.domain.KafkaMessageRecord;

public class BookingEmailRequestRecordProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailRequestRecordProcessor.class);

    private final BookingEmailRequestProcessorService bookingEmailRequestProcessorService;

    public BookingEmailRequestRecordProcessor(BookingEmailRequestProcessorService bookingEmailRequestProcessorService) {
        this.bookingEmailRequestProcessorService = bookingEmailRequestProcessorService;
    }

    public void processRecord(ConsumerRecord<String, KafkaMessageRecord> record) {
        LOGGER.info("Record consumed from topic={} partition={} offset={}", record.topic(), record.partition(), record.offset());

        bookingEmailRequestProcessorService.processRequest(record.value().getPayload());
    }

}
