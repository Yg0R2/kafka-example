package yg0r2.kafka.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(builder = KafkaMessageRecord.Builder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class KafkaMessageRecord<T> {

    public static final KafkaMessageRecord NULL_OBJECT = new Builder().build();

    private final T payload;
    private final LocalDateTime createDateTime;

    public KafkaMessageRecord(Builder<T> builder) {
        payload = builder.payload;
        createDateTime = builder.createDateTime;
    }

    public T getPayload() {
        return payload;
    }

    public LocalDateTime getCreateDateTime() {
        return createDateTime;
    }

    public static class Builder<T> {

        private T payload;
        private LocalDateTime createDateTime;

        public Builder() {
        }

        public Builder(KafkaMessageRecord<T> kafkaMessageRecord) {
            payload = kafkaMessageRecord.getPayload();
            createDateTime = kafkaMessageRecord.getCreateDateTime();
        }

        public Builder<T> withPayload(T payload) {
            this.payload = payload;

            return this;
        }

        public Builder<T> withCreateDateTime(LocalDateTime createDateTime) {
            this.createDateTime = createDateTime;

            return this;
        }

        public KafkaMessageRecord<T> build() {
            return new KafkaMessageRecord<>(this);
        }
    }

}
