package yg0r2.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.Request;

@Component
public class DefaultRequestProcessor implements RequestProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultRequestProcessor.class);

    @Override
    public void processRequest(Request request) {
        LOGGER.info("Processing request: {}", request);
    }

}
