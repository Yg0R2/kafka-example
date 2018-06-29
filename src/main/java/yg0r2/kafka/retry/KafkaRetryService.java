package yg0r2.kafka.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.producer.DefaultSlowLaneBookingEmailRequestSubmitter;
import yg0r2.kafka.retry.domain.RetryContext;
import yg0r2.kafka.retry.policy.RetryPolicy;

@Component
public class KafkaRetryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaRetryService.class);

    @Autowired
    private RetryPolicy retryPolicy;
    @Autowired
    private RetryContextTransformer retryContextTransformer;
    @Autowired
    private DefaultSlowLaneBookingEmailRequestSubmitter slowLaneBookingEmailRequestSubmitter;

    public boolean isAllowedToExecuteRetry(KafkaMessageRecord kafkaMessageRecord) {
        RetryContext retryContext = createRetryContext(kafkaMessageRecord, null);

        return retryPolicy.isAllowedToExecuteRetry(retryContext);
    }

    public void handleRetry(KafkaMessageRecord kafkaMessageRecord, Throwable throwable) {
        String payload = kafkaMessageRecord.getPayload();
        if (isRetryAllowed(kafkaMessageRecord, throwable)) {
            LOGGER.info("Resubmitting failed request payload={}", payload);

            slowLaneBookingEmailRequestSubmitter.submitEmailRequest(updateNextRetry(kafkaMessageRecord));
        } else {
            LOGGER.error("Request procession failed", throwable);

            LOGGER.info("Dropping email request payload={}", payload);
        }
    }

    private boolean isRetryAllowed(KafkaMessageRecord kafkaMessageRecord, Throwable throwable) {
        RetryContext retryContext = createRetryContext(kafkaMessageRecord, throwable);

        return retryPolicy.isRetryAllowed(retryContext);
    }

    private KafkaMessageRecord updateNextRetry(KafkaMessageRecord kafkaMessageRecord) {
        RetryContext retryContext = createRetryContext(kafkaMessageRecord, null);

        return new KafkaMessageRecord.Builder(kafkaMessageRecord)
            .withNextRetryDateTime(retryPolicy.getNextRetryDateTime(retryContext))
            .withRetryCount(kafkaMessageRecord.getRetryCount() + 1)
            .build();
    }

    private RetryContext createRetryContext(KafkaMessageRecord kafkaMessageRecord, Throwable throwable) {
        return retryContextTransformer.transform(kafkaMessageRecord, throwable);
    }

}
