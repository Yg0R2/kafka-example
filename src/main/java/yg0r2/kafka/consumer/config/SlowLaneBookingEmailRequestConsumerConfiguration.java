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
import yg0r2.kafka.serialization.KafkaMessageRecordDeserializer;
import yg0r2.kafka.service.BookingEmailRequestRecordProcessor;

@Configuration
public class SlowLaneBookingEmailRequestConsumerConfiguration {

    @Value("${kafka.bootstrap-servers}")
    private String brokers;
    @Value("${kafka.groupId}")
    private String groupId;
    @Value("${kafka.slowLane.topic}")
    private String topic;
    @Value("${kafka.slowLane.poll.max.records}")
    private int pollMaxRecords;
    @Value("${kafka.slowLane.poll.timeout.ms}")
    private long pollTimeout;

    @Autowired
    @Qualifier(value = "slowLaneBookingEmailRequestRecordProcessor")
    private BookingEmailRequestRecordProcessor slowLaneBookingEmailRequestRecordProcessor;

    @Bean("slowLaneBookingEmailRequestConsumer")
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public BookingEmailRequestConsumer slowLaneBookingEmailRequestConsumer(
        @Qualifier(value = "slowLaneKafkaConsumer") Consumer<String, KafkaMessageRecord> slowLaneKafkaConsumer) {

        return new BookingEmailRequestConsumer(slowLaneBookingEmailRequestRecordProcessor, slowLaneKafkaConsumer, topic, pollTimeout);
    }

    @Bean("slowLaneKafkaConsumer")
    @Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Consumer<String, KafkaMessageRecord> slowLaneKafkaConsumer() {
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
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaMessageRecordDeserializer.class);

        return props;
    }

}

