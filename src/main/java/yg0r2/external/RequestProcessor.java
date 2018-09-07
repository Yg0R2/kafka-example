package yg0r2.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.Request;

@Component
public class RequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(RequestProcessor.class);

    public void processRequest(Request request) {
        LOGGER.info("Processing request: {}", request);
    }

}
