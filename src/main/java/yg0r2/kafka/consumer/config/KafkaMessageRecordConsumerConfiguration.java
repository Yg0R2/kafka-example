package yg0r2.kafka.consumer.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.serialization.KafkaMessageRecordDeserializer;
import yg0r2.kafka.serialization.RequestCorrelationIdDeserializer;

@Configuration
public class KafkaMessageRecordConsumerConfiguration {

    @Value("${kafka.brokers}")
    private String brokers;
    @Value("${kafka.group.id}")
    private String groupId;
    @Value("${kafka.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.topic}")
    private String topic;

    @Bean
    public Consumer<RequestCorrelationId, KafkaMessageRecord> kafkaConsumer() {
        Consumer<RequestCorrelationId, KafkaMessageRecord> consumer = new KafkaConsumer<>(consumerConfigs());

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, RequestCorrelationIdDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageRecordDeserializer.class);

        return props;
    }

}
