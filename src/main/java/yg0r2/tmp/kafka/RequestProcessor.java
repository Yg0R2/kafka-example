package yg0r2.tmp.kafka;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import net.jodah.failsafe.CircuitBreakerOpenException;

@Component
public class RequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);
    private static Map<String, Integer> COUNTER_MAP = new HashMap<>();

    @Autowired
    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    public void handleRequest(String payload) {
        LOGGER.info("received payload='{}'", payload);

        if (!COUNTER_MAP.containsKey(payload)) {
            COUNTER_MAP.put(payload, 0);
        }

        int counter = COUNTER_MAP.get(payload);
        if (counter < 5) {
            COUNTER_MAP.put(payload, ++counter);

            LOGGER.error("CircuitBreakerOpenException");

            //resubmitProcessor.resubmit(payload);

            throw new CircuitBreakerOpenException();
            //throw new RuntimeException("haha");
        }

        LOGGER.info("handle payload='{}'", payload);
    }

}
