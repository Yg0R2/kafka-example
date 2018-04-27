package yg0r2.tmp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class ErrorHandler implements org.springframework.kafka.listener.ErrorHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorHandler.class);

    private SlowLaneResubmitProcessor slowLaneResubmitProcessor;

    public ErrorHandler(SlowLaneResubmitProcessor slowLaneResubmitProcessor) {
        this.slowLaneResubmitProcessor = slowLaneResubmitProcessor;
    }

    @Override
    public void handle(Exception thrownException, ConsumerRecord<?, ?> data) {
        LOGGER.info("Error happened {}", thrownException.getMessage());

        LOGGER.info("data: {}", data);

        slowLaneResubmitProcessor.resubmit((String) data.value());
    }
}
