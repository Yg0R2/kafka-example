package yg0r2.kafka.retry.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public class RetryContext {

    private final LocalDateTime requestCreateDateTime;
    private final LocalDateTime requestNextRetryDateTime;
    private final int retryCount;
    private final Throwable throwable;

    private RetryContext(Builder builder) {
        requestCreateDateTime = Optional.ofNullable(builder.requestCreateDateTime).orElse(LocalDateTime.now());
        requestNextRetryDateTime = Optional.ofNullable(builder.requestNextRetryDateTime).orElse(LocalDateTime.now());
        retryCount = Optional.ofNullable(builder.retryCount).orElse(0);
        throwable = builder.throwable;
    }

    public LocalDateTime getRequestCreateDateTime() {
        return requestCreateDateTime;
    }

    public LocalDateTime getRequestNextRetryDateTime() {
        return requestNextRetryDateTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public static class Builder {

        private LocalDateTime requestCreateDateTime;
        private LocalDateTime requestNextRetryDateTime;
        private Integer retryCount;
        private Throwable throwable;

        public Builder withRequestCreateDateTime(LocalDateTime requestCreateDateTime) {
            this.requestCreateDateTime = requestCreateDateTime;

            return this;
        }

        public Builder withRequestNextRetryDateTime(LocalDateTime requestNextRetryDateTime) {
            this.requestNextRetryDateTime = requestNextRetryDateTime;

            return this;
        }

        public Builder withRetryCount(Integer retryCount) {
            this.retryCount = retryCount;

            return this;
        }

        public Builder withThrowable(Throwable throwable) {
            this.throwable = throwable;

            return this;
        }

        public RetryContext build() {
            return new RetryContext(this);
        }

    }

}
