package yg0r2.kafka.retry;

import yg0r2.kafka.retry.domain.RetryContext;

public interface RetryPolicy {

    boolean canRetry(RetryContext retryContext);

}
