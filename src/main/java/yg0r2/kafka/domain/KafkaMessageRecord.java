package yg0r2.kafka.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

@JsonDeserialize(builder = KafkaMessageRecord.Builder.class)
public final class KafkaMessageRecord {

    public static final KafkaMessageRecord EMPTY_OBJECT = new KafkaMessageRecord.Builder().build();

    private final Request request;
    private final LocalDateTime createDateTime;
    private final LocalDateTime nextRetryDateTime;
    private final int retryCount;

    private KafkaMessageRecord(Builder builder) {
        request = builder.request;
        createDateTime = Optional.ofNullable(builder.createDateTime).orElse(LocalDateTime.now());
        nextRetryDateTime = builder.nextRetryDateTime;
        retryCount = Optional.ofNullable(builder.retryCount).orElse(0);
    }

    public Request getRequest() {
        return request;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public LocalDateTime getNextRetryDateTime() {
        return nextRetryDateTime;
    }

    public int getRetryCount() {
        return retryCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        KafkaMessageRecord that = (KafkaMessageRecord) o;
        return Objects.equal(request, that.request) &&
            Objects.equal(createDateTime, that.createDateTime) &&
            Objects.equal(nextRetryDateTime, that.nextRetryDateTime) &&
            Objects.equal(retryCount, that.retryCount);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(request, createDateTime, nextRetryDateTime, retryCount);
    }

    @Override
    public String toString() {
        return "KafkaMessageRecord{" +
            "request=" + request +
            ", createDateTime=" + createDateTime +
            ", nextRetryDateTime=" + nextRetryDateTime +
            ", retryCount=" + retryCount +
            '}';
    }

    public static class Builder {

        private Request request;
        private LocalDateTime createDateTime;
        private LocalDateTime nextRetryDateTime;
        private Integer retryCount;

        public Builder() {
        }

        public Builder(KafkaMessageRecord kafkaMessageRecord) {
            request = kafkaMessageRecord.getRequest();
            createDateTime = kafkaMessageRecord.createDateTime;
            nextRetryDateTime = kafkaMessageRecord.nextRetryDateTime;
            retryCount = kafkaMessageRecord.retryCount;
        }

        public Builder withRequest(Request request) {
            this.request = request;

            return this;
        }

        public Builder withCreateDateTime(LocalDateTime createDateTime) {
            this.createDateTime = createDateTime;

            return this;
        }

        public Builder withNextRetryDateTime(LocalDateTime nextRetryDateTime) {
            this.nextRetryDateTime = nextRetryDateTime;

            return this;
        }

        public Builder withRetryCount(Integer retryCount) {
            this.retryCount = retryCount;

            return this;
        }

        public KafkaMessageRecord build() {
            return new KafkaMessageRecord(this);
        }
    }

}
