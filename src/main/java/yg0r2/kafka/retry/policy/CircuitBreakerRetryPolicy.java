package yg0r2.kafka.retry.policy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.jodah.failsafe.CircuitBreakerOpenException;
import yg0r2.kafka.retry.domain.RetryContext;

public class CircuitBreakerRetryPolicy extends IncrementalRetryPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(CircuitBreakerRetryPolicy.class);

    private final RetryPolicy delegate;

    public CircuitBreakerRetryPolicy(RetryPolicy delegate, Long initialRetentionSeconds) {
        super(delegate, initialRetentionSeconds);

        this.delegate = delegate;
    }

    @Override
    public boolean isRetryAllowed(RetryContext retryContext) {
        boolean isCircuitBreakerOpen = false;

        if (retryContext.getThrowable() instanceof CircuitBreakerOpenException) {
            isCircuitBreakerOpen = true;

            LOGGER.info("Circuit breaker is open, scheduling retry.");
        }

        return isCircuitBreakerOpen || delegate.isRetryAllowed(retryContext);
    }

}
