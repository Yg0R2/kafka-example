package yg0r2.kafka.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class BookingEmailRequestProcessorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookingEmailRequestProcessorService.class);
    private static final Map<String, Integer> COUNTER_MAP = new HashMap<>();
    private static final Random RND = new Random();

    public void processRequest(String payload) {
        try {
            Thread.sleep(RND.nextInt(1000) + 500);
        }
        catch (InterruptedException e) {
        }

        int counter = COUNTER_MAP.putIfAbsent(payload, 0);
        if (counter < 5) {
            COUNTER_MAP.put(payload, ++counter);

            LOGGER.error("Expected fails is 5 and actual is " + counter + " for payload=" + payload);
            throw new RuntimeException();
        }

        LOGGER.info("Handled payload={}", payload);
    }

}
