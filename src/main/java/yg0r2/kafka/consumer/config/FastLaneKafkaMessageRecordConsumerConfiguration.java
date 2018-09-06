package yg0r2.kafka.consumer.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yg0r2.kafka.consumer.KafkaMessageRecordConsumer;
import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.processor.KafkaMessageRecordProcessor;
import yg0r2.kafka.serialization.RequestCorrelationIdDeserializer;
import yg0r2.kafka.serialization.RequestDeserializer;

@Configuration
public class FastLaneKafkaMessageRecordConsumerConfiguration {

    @Value("${kafka.brokers}")
    private String brokers;
    @Value("${kafka.group.id}")
    private String groupId;
    @Value("${kafka.auto.offset.reset}")
    private String autoOffsetReset;
    @Value("${kafka.fastLane.topic}")
    private String topic;
    @Value("${kafka.fastLane.pollTimeout}")
    private long pollTimeout;

    @Autowired
    private KafkaMessageRecordProcessor kafkaMessageRecordProcessor;

    @Bean
    public KafkaMessageRecordConsumer fastLaneKafkaMessageRecordConsumer() {
        return new KafkaMessageRecordConsumer(fastLaneKafkaConsumer(), kafkaMessageRecordProcessor, topic, pollTimeout);
    }

    @Bean
    public Consumer<RequestCorrelationId, KafkaMessageRecord> fastLaneKafkaConsumer() {
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
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, RequestDeserializer.class);

        return props;
    }

}
