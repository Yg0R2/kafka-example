package yg0r2.kafka.retry.domain;

import yg0r2.kafka.domain.KafkaMessageRecord;

public class RetryContext {

    private final KafkaMessageRecord<String> messageRecord;
    private final Throwable throwable;

    private RetryContext(Builder builder) {
        messageRecord = builder.messageRecord;
        throwable = builder.throwable;
    }

    public KafkaMessageRecord<String> getMessageRecord() {
        return messageRecord;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public static class Builder {

        private KafkaMessageRecord<String> messageRecord;
        private Throwable throwable;

        public Builder withMessageRecord(KafkaMessageRecord<String> messageRecord) {
            this.messageRecord = messageRecord;

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
