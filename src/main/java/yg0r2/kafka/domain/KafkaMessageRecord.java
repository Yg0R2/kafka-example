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

    private KafkaMessageRecord(Builder builder) {
        payload = builder.payload;
        createDateTime = Optional.ofNullable(builder.createDateTime).orElse(LocalDateTime.now());
    }

    public String getPayload() {
        return payload;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public static class Builder {

        private String payload;
        private LocalDateTime createDateTime;

        public Builder withPayload(String payload) {
            this.payload = payload;

            return this;
        }

        public Builder withCreateDateTime(LocalDateTime createDateTime) {
            this.createDateTime = createDateTime;

            return this;
        }

        public KafkaMessageRecord build() {
            return new KafkaMessageRecord(this);
        }
    }

}
