package yg0r2.kafka.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import net.jodah.failsafe.CircuitBreakerOpenException;
import yg0r2.kafka.domain.KafkaMessageRecord;

@Component
public class BookingEmailRequestProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailRequestProcessorService.class);
    private static final Map<String, Integer> COUNTER_MAP = new HashMap<>();
    private static final Random RND = new Random();
    private static final int MAX_FAIL_COUNT = 5;

    public void processRequest(String payload) {
        try {
            Thread.sleep(RND.nextInt(1000) + 500);
        }
        catch (InterruptedException e) {
        }

        if (RND.nextBoolean()) {
            LOGGER.error("Circuit breaker open; payload={}", payload);

            throw new CircuitBreakerOpenException();
        }

        int counter = COUNTER_MAP.putIfAbsent(payload, 0);
        if (counter < MAX_FAIL_COUNT) {
            COUNTER_MAP.put(payload, ++counter);

            LOGGER.error("Expected fails is {} and actual is {} for payload={}", MAX_FAIL_COUNT, counter, payload);
            throw new RuntimeException();
        }

        LOGGER.info("Handled payload={}", payload);
    }

}
