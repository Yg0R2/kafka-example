package yg0r2.kafka.retry.policy;

import java.time.LocalDateTime;
import java.util.Optional;

import yg0r2.kafka.retry.domain.RetryContext;

public class IncrementalRetryPolicy implements RetryPolicy {

    private final RetryPolicy delegate;
    private final Long initialRetentionSeconds;

    public IncrementalRetryPolicy(RetryPolicy delegate, long initialRetentionSeconds) {
        this.delegate = delegate;
        this.initialRetentionSeconds = initialRetentionSeconds;
    }

    @Override
    public boolean isAllowedToExecuteRetry(RetryContext retryContext) {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(retryContext.getRequestNextRetryDateTime()) && delegate.isAllowedToExecuteRetry(retryContext);
    }

    @Override
    public boolean isRetryAllowed(RetryContext retryContext) {
        LocalDateTime nextRetryDateTime = retryContext.getRequestNextRetryDateTime();

        return nextRetryDateTime.isAfter(LocalDateTime.now()) && delegate.isRetryAllowed(retryContext);
    }

    @Override
    public LocalDateTime getNextRetryDateTime(RetryContext retryContext) {
        return Optional.of(retryContext.getRequestNextRetryDateTime())
            .filter(nextRetry -> nextRetry.isBefore(LocalDateTime.now()))
            .orElse(calculateNextRetry(retryContext.getRetryCount()));
    }

    private LocalDateTime calculateNextRetry(Integer retryCount) {
        long nextRetry = (long) Math.pow(initialRetentionSeconds.doubleValue(), retryCount.doubleValue());

        return LocalDateTime.now().plusSeconds(nextRetry);
    }

}
