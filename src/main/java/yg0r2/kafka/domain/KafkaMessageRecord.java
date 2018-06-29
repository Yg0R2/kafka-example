package yg0r2.kafka.domain;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = KafkaMessageRecord.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaMessageRecord {

    public static final KafkaMessageRecord NULL_OBJECT = new Builder().build();

    private final String payload;
    private final LocalDateTime createDateTime;
    private final LocalDateTime nextRetryDateTime;
    private final int retryCount;

    private KafkaMessageRecord(Builder builder) {
        payload = Optional.ofNullable(builder.payload).orElse("");
        createDateTime = Optional.ofNullable(builder.createDateTime).orElse(LocalDateTime.now());
        nextRetryDateTime = builder.nextRetryDateTime;
        retryCount = Optional.ofNullable(builder.retryCount).orElse(0);
    }

    public String getPayload() {
        return payload;
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

    public static class Builder {

        private String payload;
        private LocalDateTime createDateTime;
        private LocalDateTime nextRetryDateTime;
        private Integer retryCount;

        public Builder() {
        }

        public Builder(KafkaMessageRecord kafkaMessageRecord) {
            payload = kafkaMessageRecord.getPayload();
            createDateTime = kafkaMessageRecord.createDateTime;
            nextRetryDateTime = kafkaMessageRecord.nextRetryDateTime;
            retryCount = kafkaMessageRecord.retryCount;
        }

        public Builder withPayload(String payload) {
            this.payload = payload;

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
