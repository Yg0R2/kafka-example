package yg0r2.kafka.retry.policy;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.retry.domain.RetryContext;

public class FixEndDateTimeRetryPolicy implements RetryPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(FixEndDateTimeRetryPolicy.class);

    private final long retentionMaxSeconds;

    public FixEndDateTimeRetryPolicy(long retentionMaxSeconds) {
        this.retentionMaxSeconds = retentionMaxSeconds;
    }

    @Override
    public boolean canExecuteRetry(RetryContext retryContext) {
        return true;
    }

    @Override
    public boolean canRescheduleFailedRetry(RetryContext retryContext) {
        LocalDateTime createDateTime = retryContext.getRequestCreateDateTime();
        LocalDateTime endDateTime = getEndDateTime(createDateTime);

        boolean isCreateTimeBeforeEndTime = createDateTime.isBefore(endDateTime);

        if (isCreateTimeBeforeEndTime) {
            LOGGER.info("Create time is before then end time, scheduling retry.");
        }

        return isCreateTimeBeforeEndTime;
    }

    @Override
    public LocalDateTime getNextRetryDateTime(RetryContext retryContext) {
        return null;
    }

    private LocalDateTime getEndDateTime(LocalDateTime createDateTime) {
        return createDateTime.plusSeconds(retentionMaxSeconds);
    }

}
