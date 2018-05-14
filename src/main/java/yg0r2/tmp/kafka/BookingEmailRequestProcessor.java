package yg0r2.tmp.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookingEmailRequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailRequestProcessor.class);
    private static Map<String, Integer> COUNTER_MAP = new HashMap<>();

    public void processRecords(List<ConsumerRecord<String, String>> records) {
        records.forEach(this::processRecord);
    }

    private void processRecord(ConsumerRecord<String, String> record) {
        this.processPayload(record.value());
    }

    private void processPayload(String payload) {
        LOGGER.info("Processing payload={}", payload);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (!COUNTER_MAP.containsKey(payload)) {
            COUNTER_MAP.put(payload, 0);
        }

        int counter = COUNTER_MAP.get(payload);
        if (counter < 10) {
            COUNTER_MAP.put(payload, ++counter);

            throw new RuntimeException("Failed count doesn't meet.");
        }

        LOGGER.info("Handled payload={}", payload);
    }

}
