package yg0r2.kafka.retry.policy;

import java.time.LocalDateTime;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jodah.failsafe.CircuitBreakerOpenException;
import yg0r2.kafka.retry.domain.RetryContext;

public class CircuitBreakerRetryPolicy extends IncrementalRetryPolicy {

    private static final String IS_CIRCUIT_BREAKER_OPEN_TRUE = "isCircuitBreakerOpen=true";

    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerRetryPolicy.class);

    private final RetryPolicy delegate;

    public CircuitBreakerRetryPolicy(RetryPolicy delegate, Long initialRetentionSeconds) {
        super(delegate, initialRetentionSeconds);

        this.delegate = delegate;
    }

    @Override
    public boolean canExecuteRetry(RetryContext retryContext) {
        LocalDateTime now = LocalDateTime.now();

        return now.isAfter(retryContext.getRequestNextRetryDateTime()) && delegate.canExecuteRetry(retryContext);
    }

    @Override
    public boolean canRescheduleFailedRetry(RetryContext retryContext) {
        boolean isCircuitBreakerOpen = isCircuitBreakerOpen(retryContext.getThrowable());

        if (isCircuitBreakerOpen) {
            LOGGER.info("Circuit breaker is open, scheduling retry.");
        }

        return isCircuitBreakerOpen || delegate.canRescheduleFailedRetry(retryContext);
    }

    @Override
    public LocalDateTime getNextRetryDateTime(RetryContext retryContext) {
        LocalDateTime nextRetDateTime;

        if (isCircuitBreakerOpen(retryContext.getThrowable())) {
            nextRetDateTime = super.getNextRetryDateTime(retryContext);
        }
        else {
            nextRetDateTime = delegate.getNextRetryDateTime(retryContext);
        }

        return nextRetDateTime;
    }

    private boolean isCircuitBreakerOpen(Throwable throwable) {
        return (throwable instanceof CircuitBreakerOpenException) ||
            Optional.ofNullable(throwable.getMessage())
                .map(message -> message.contains(IS_CIRCUIT_BREAKER_OPEN_TRUE))
                .orElse(false);
    }

}
