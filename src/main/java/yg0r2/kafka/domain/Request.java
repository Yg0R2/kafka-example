package yg0r2.kafka.domain;

import java.util.UUID;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

@JsonDeserialize(builder = Request.Builder.class)
public final class Request {

    public static final Request EMPTY_OBJECT = new Request.Builder().build();

    private final UUID requestId;
    private final long timestamp;
    private final String value;

    private Request(Builder builder) {
        requestId = builder.requestId;
        timestamp = builder.timestamp;
        value = builder.value;
    }

    public UUID getRequestId() {
        return requestId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((o == null) || (getClass() != o.getClass())) {
            return false;
        }

        Request that = (Request) o;
        return timestamp == that.timestamp &&
            Objects.equal(requestId, that.requestId) &&
            Objects.equal(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(requestId, timestamp, value);
    }

    @Override
    public String toString() {
        return "Request{" +
            "requestId=" + requestId +
            ", timestamp=" + timestamp +
            ", value='" + value + '\'' +
            '}';
    }

    public static class Builder {

        private UUID requestId;
        private long timestamp;
        private String value;

        public Builder withRequestId(UUID requestId) {
            this.requestId = requestId;

            return this;
        }

        public Builder withTimestamp(long timestamp) {
            this.timestamp = timestamp;

            return this;
        }

        public Builder withValue(String value) {
            this.value = value;

            return this;
        }

        public Request build() {
            return new Request(this);
        }

    }

}
