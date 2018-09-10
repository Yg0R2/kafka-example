package yg0r2.kafka.retry.policy;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.util.Assert;

import yg0r2.kafka.retry.domain.RetryContext;

public class ExcludedExceptionRetryPolicy implements RetryPolicy {

    private final RetryPolicy delegate;

    private final List<Class<? extends Throwable>> excludedExceptions;

    public ExcludedExceptionRetryPolicy(RetryPolicy delegate, List<Class<? extends Throwable>> excludedExceptions) {
        Assert.notNull(delegate, "RetryPolicy should not be null.");

        this.delegate = delegate;
        this.excludedExceptions = excludedExceptions;
    }

    @Override
    public boolean canExecuteRetry(RetryContext retryContext) {
        return true;
    }

    @Override
    public boolean canRescheduleFailedRetry(RetryContext retryContext) {
        Throwable throwable = retryContext.getThrowable();

        return !isExcludedException(throwable.getClass()) && delegate.canRescheduleFailedRetry(retryContext);
    }

    @Override
    public LocalDateTime getNextRetryDateTime(RetryContext retryContext) {
        return delegate.getNextRetryDateTime(retryContext);
    }

    private boolean isExcludedException(Class<? extends Throwable> clazz) {
        return excludedExceptions.stream()
            .anyMatch(excludedExceptionClazz -> excludedExceptionClazz.isAssignableFrom(clazz));
    }

}
