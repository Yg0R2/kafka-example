package yg0r2.kafka.retry;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.retry.domain.RetryContext;

public class TimeRetryPolicy implements RetryPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeRetryPolicy.class);

    private final long retryMaxSeconds;

    public TimeRetryPolicy(long retryMaxSeconds) {
        this.retryMaxSeconds = retryMaxSeconds;
    }

    @Override
    public boolean canRetry(RetryContext retryContext) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endTime = getEndTime(retryContext.getMessageRecord());

        boolean isBefore = now.isBefore(endTime);

        LOGGER.info("Current time: " + now + ", can retry till: " + endTime + ", can retry: " + isBefore);

        return isBefore;
    }

    private LocalDateTime getEndTime(KafkaMessageRecord<String> messageRecord) {
        LocalDateTime createDateTime = messageRecord.getCreateDateTime();

        return createDateTime.plusSeconds(retryMaxSeconds);
    }

}
