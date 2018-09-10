package yg0r2.kafka.retry;

import org.springframework.stereotype.Component;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.retry.domain.RetryContext;

@Component
public class RetryContextTransformer {

    public RetryContext transform(KafkaMessageRecord kafkaMessageRecord) {
        return transform(kafkaMessageRecord, null);
    }

    public RetryContext transform(KafkaMessageRecord kafkaMessageRecord, Throwable throwable) {
        return new RetryContext.Builder()
            .withRequestCreateDateTime(kafkaMessageRecord.getCreateDateTime())
            .withRequestNextRetryDateTime(kafkaMessageRecord.getNextRetryDateTime())
            .withRetryCount(kafkaMessageRecord.getRetryCount())
            .withThrowable(throwable)
            .build();
    }

}
