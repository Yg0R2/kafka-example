package yg0r2.kafka.retry.policy;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import yg0r2.kafka.retry.domain.RetryContext;

public class ExcludedExceptionRetryPolicy implements RetryPolicy {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcludedExceptionRetryPolicy.class);

    private final RetryPolicy delegate;

    private final List<Class<? extends Throwable>> excludedExceptions;

    public ExcludedExceptionRetryPolicy(RetryPolicy delegate, List<Class<? extends Throwable>> excludedExceptions) {
        this.delegate = delegate;
        this.excludedExceptions = excludedExceptions;
    }

    @Override
    public boolean isAllowedToExecuteRetry(RetryContext retryContext) {
        return true;
    }

    @Override
    public boolean isRetryAllowed(RetryContext retryContext) {
        Throwable throwable = retryContext.getThrowable();

        boolean isExcludedException = excludedExceptions.contains(throwable.getClass());

        if (isExcludedException) {
            LOGGER.error("Request procession failed, ignoring request", throwable);
        }

        return !isExcludedException && delegate.isRetryAllowed(retryContext);
    }

    @Override
    public LocalDateTime getNextRetryDateTime(RetryContext retryContext) {
        return delegate.getNextRetryDateTime(retryContext);
    }
}
