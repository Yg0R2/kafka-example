package yg0r2.kafka.producer;

public interface KafkaMessageRecordProducer<T> {

    void submit(T request);

}
