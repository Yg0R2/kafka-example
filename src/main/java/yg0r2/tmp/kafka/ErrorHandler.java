package yg0r2.tmp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorHandler implements org.springframework.kafka.listener.ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    public ErrorHandler(SlowLaneResubmitProcessor slowLaneResubmitProcessor) {
        this.slowLaneResubmitProcessor = slowLaneResubmitProcessor;
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        LOGGER.info("Error happened: {}", thrownException.getMessage());

        slowLaneResubmitProcessor.resubmit(data);
    }
}
