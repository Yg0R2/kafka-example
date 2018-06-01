package yg0r2.tmp.kafka;

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

        if (!COUNTER_MAP.containsKey(payload)) {
            COUNTER_MAP.put(payload, 0);
        }

        int counter = COUNTER_MAP.get(payload);
        if (counter < 5) {
            COUNTER_MAP.put(payload, ++counter);

            throw new RuntimeException("Failed count doesn't meet.");
        }

        LOGGER.info("Handled payload={}", payload);
    }

}
