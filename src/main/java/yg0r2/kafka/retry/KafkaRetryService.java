package yg0r2.kafka.retry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.Request;
import yg0r2.kafka.producer.KafkaMessageRecordProducer;
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
    private KafkaMessageRecordProducer<KafkaMessageRecord> slowLaneKafkaMessageRecordProducer;

    public boolean canExecuteRetry(KafkaMessageRecord kafkaMessageRecord) {
        RetryContext retryContext = createRetryContext(kafkaMessageRecord, null);

        return retryPolicy.canExecuteRetry(retryContext);
    }

    public void handleFailedRetry(KafkaMessageRecord kafkaMessageRecord, Throwable throwable) {
        RetryContext retryContext = createRetryContext(kafkaMessageRecord, throwable);
        Request request = kafkaMessageRecord.getRequest();

        LOGGER.error("Request procession failed", throwable);

        if (canRescheduleFailedRetry(retryContext)) {
            slowLaneKafkaMessageRecordProducer.submit(createUpdatedKafkaMessageRecord(kafkaMessageRecord, retryContext));
        } else {
            LOGGER.info("Dropping email request request={}", request);
        }
    }

    private boolean canRescheduleFailedRetry(RetryContext retryContext) {
        return retryPolicy.canRescheduleFailedRetry(retryContext);
    }

    private KafkaMessageRecord createUpdatedKafkaMessageRecord(KafkaMessageRecord kafkaMessageRecord, RetryContext retryContext) {
        return new KafkaMessageRecord.Builder(kafkaMessageRecord)
            .withNextRetryDateTime(retryPolicy.getNextRetryDateTime(retryContext))
            .withRetryCount(kafkaMessageRecord.getRetryCount() + 1)
            .build();
    }

    private RetryContext createRetryContext(KafkaMessageRecord kafkaMessageRecord, Throwable throwable) {
        return retryContextTransformer.transform(kafkaMessageRecord, throwable);
    }

}
