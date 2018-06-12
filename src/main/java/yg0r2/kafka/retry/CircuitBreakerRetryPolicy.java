package yg0r2.kafka.retry;

import java.util.Objects;

import net.jodah.failsafe.CircuitBreakerOpenException;
import yg0r2.kafka.retry.domain.RetryContext;

public class CircuitBreakerRetryPolicy implements RetryPolicy {

    private final RetryPolicy delegate;

    public CircuitBreakerRetryPolicy(RetryPolicy delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean canRetry(RetryContext retryContext) {
        Throwable throwable = retryContext.getThrowable();

        return (throwable instanceof CircuitBreakerOpenException) || (Objects.nonNull(delegate) && delegate.canRetry(retryContext));
    }
}
