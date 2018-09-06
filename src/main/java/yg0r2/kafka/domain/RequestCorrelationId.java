package yg0r2.kafka.domain;

import java.util.Objects;
import java.util.UUID;

public class RequestCorrelationId {

    public static final RequestCorrelationId EMPTY_OBJECT = new RequestCorrelationId(null, 0);

    private final UUID requestId;
    private final long timestamp;

    public RequestCorrelationId(UUID requestId, long timestamp) {
        this.requestId = requestId;
        this.timestamp = timestamp;
    }

    public UUID getRequestId() {
        return requestId;
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

        RequestCorrelationId that = (RequestCorrelationId) o;
        return timestamp == that.timestamp &&
            Objects.equals(requestId, that.requestId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestId, timestamp);
    }

    @Override
    public String toString() {
        return "RequestCorrelationId{" + "requestId=" + requestId + ", timestamp=" + timestamp + '}';
    }

}
