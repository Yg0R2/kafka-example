package yg0r2.kafka.producer.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.domain.RequestCorrelationId;
import yg0r2.kafka.serialization.KafkaMessageRecordSerializer;
import yg0r2.kafka.serialization.RequestCorrelationIdSerializer;

@Configuration
public class FastLaneKafkaMessageRecordProducerConfiguration {

    @Value("${kafka.brokers}")
    private String brokers;
    @Value("${kafka.fastLane.topic}")
    private String topic;

    @Bean
    public KafkaTemplate<RequestCorrelationId, KafkaMessageRecord> fastLaneKafkaTemplate() {
        KafkaTemplate<RequestCorrelationId, KafkaMessageRecord> kafkaTemplate = new KafkaTemplate<>(producerFactory());

        kafkaTemplate.setDefaultTopic(topic);

        return kafkaTemplate;
    }

    private ProducerFactory<RequestCorrelationId, KafkaMessageRecord> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    private Map<String, Object> producerConfigs() {
        Map<String, Object> properties = new HashMap<>();

        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        properties.put(ProducerConfig.RETRIES_CONFIG, 0);
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        properties.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, RequestCorrelationIdSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaMessageRecordSerializer.class);

        return properties;
    }
}
