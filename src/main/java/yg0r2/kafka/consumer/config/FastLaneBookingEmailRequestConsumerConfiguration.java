package yg0r2.kafka.consumer.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import yg0r2.kafka.consumer.BookingEmailRequestConsumer;
import yg0r2.kafka.domain.KafkaMessageRecord;
import yg0r2.kafka.service.BookingEmailRequestRecordProcessor;
import yg0r2.kafka.serialization.ModifiedStringDeserializer;

@Configuration
public class FastLaneBookingEmailRequestConsumerConfiguration {

    @Value("${kafka.bootstrap-servers}")
    private String brokers;
    @Value("${kafka.groupId}")
    private String groupId;
    @Value("${kafka.fastLane.topic}")
    private String topic;
    @Value("${kafka.slowLane.poll.max.records}")
    private int pollMaxRecords;
    @Value("${kafka.fastLane.poll.timeout.ms}")
    private long pollTimeout;

    @Autowired
    @Qualifier(value = "fastLaneBookingEmailRequestRecordProcessor")
    private BookingEmailRequestRecordProcessor fastLaneBookingEmailRequestRecordProcessor;

    @Bean("fastLaneBookingEmailRequestConsumer")
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookingEmailRequestConsumer fastLaneBookingEmailRequestConsumer(
        @Qualifier(value = "fastLaneKafkaConsumer") Consumer<String, KafkaMessageRecord> fastLaneKafkaConsumer) {

        return new BookingEmailRequestConsumer(fastLaneBookingEmailRequestRecordProcessor, fastLaneKafkaConsumer, topic, pollTimeout);
    }

    @Bean("fastLaneKafkaConsumer")
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer<String, KafkaMessageRecord> fastLaneKafkaConsumer() {
        Consumer<String, KafkaMessageRecord> consumer = new KafkaConsumer<>(consumerConfigs());

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, pollMaxRecords);

        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ModifiedStringDeserializer.class);

        return props;
    }

}

