package yg0r2.kafka.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Objects;

@JsonDeserialize(builder = KafkaMessageRecord.Builder.class)
public final class KafkaMessageRecord {

    public static final KafkaMessageRecord EMPTY_OBJECT = new KafkaMessageRecord.Builder().build();

    private final String request;

    private KafkaMessageRecord(Builder builder) {
        request = builder.request;
    }

    public String getRequest() {
        return request;
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
        return Objects.equal(request, that.request);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(request);
    }

    @Override
    public String toString() {
        return "KafkaMessageRecord{" +
            "request='" + request + '\'' +
            '}';
    }

    public static class Builder {

        private String request;

        public Builder withRequest(String request) {
            this.request = request;

            return this;
        }

        public KafkaMessageRecord build() {
            return new KafkaMessageRecord(this);
        }
    }

}
