package yg0r2.kafka.retry.policy;

import java.time.LocalDateTime;

import yg0r2.kafka.retry.domain.RetryContext;

public interface RetryPolicy {

    boolean isAllowedToExecuteRetry(RetryContext retryContext);

    boolean isRetryAllowed(RetryContext retryContext);

    LocalDateTime getNextRetryDateTime(RetryContext retryContext);
}
