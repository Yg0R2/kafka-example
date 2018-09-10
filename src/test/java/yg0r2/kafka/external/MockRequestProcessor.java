package yg0r2.kafka.external;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import yg0r2.external.DefaultRequestProcessor;
import yg0r2.external.RequestProcessor;
import yg0r2.kafka.domain.Request;

@Primary
@Component
public class MockRequestProcessor implements RequestProcessor {

    private static final Map<Request, Integer> RETRIES = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestProcessor.class);

    @Override
    public void processRequest(Request request) {
        int retries = RETRIES.put(request, RETRIES.getOrDefault(request, 0) + 1);

        LOGGER.info("Retries count: {}", retries);

        if (retries < 3) {
            throw new RuntimeException();
        }

        LOGGER.info("Handled request: {}", request);
    }

}
