package yg0r2.kafka.domain;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

@JsonDeserialize(builder = KafkaMessageRecord.Builder.class)
public final class KafkaMessageRecord {

    public static final KafkaMessageRecord EMPTY_OBJECT = new KafkaMessageRecord.Builder().build();

    private final UUID requestId;
    private final String request;
    private final long timestamp;

    private KafkaMessageRecord(Builder builder) {
        requestId = builder.requestId;
        request = builder.request;
        timestamp = builder.timestamp;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public String getRequest() {
        return request;
    }

    public long getTimestamp() {
        return timestamp;
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
        return Objects.equal(requestId, that.requestId) &&
            Objects.equal(request, that.request) &&
            Objects.equal(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestId, request, timestamp);
    }

    @Override
    public String toString() {
        return "KafkaMessageRecord{" +
            "requestId='" + requestId + '\'' +
            ", request='" + request + '\'' +
            ", timestamp=" + timestamp +
            '}';
    }

    public static class Builder {

        private UUID requestId;
        private String request;
        private long timestamp;

        public Builder withRequestId(UUID requestId) {
            this.requestId = requestId;

            return this;
        }

        public Builder withRequest(String request) {
            this.request = request;

            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;

            return this;
        }

        public KafkaMessageRecord build() {
            return new KafkaMessageRecord(this);
        }
    }

}
