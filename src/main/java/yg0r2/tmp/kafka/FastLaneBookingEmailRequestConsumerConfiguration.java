package yg0r2.tmp.kafka;

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

@Configuration
@Scope(scopeName = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FastLaneBookingEmailRequestConsumerConfiguration {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.groupId}")
    private String groupId;
    @Value("${kafka.fastLane.topic}")
    private String topic;
    @Value("${kafka.fastLane.poll.timeout.ms}")
    private long pollTimeout;

    @Autowired
    @Qualifier(value = "fastLaneBookingEmailRequestProcessor")
    private BookingEmailRequestProcessor fastLaneBookingEmailRequestProcessor;

    @Bean("fastLaneBookingEmailRequestConsumer")
    public BookingEmailRequestConsumer fastLaneBookingEmailRequestConsumer() {
        return new BookingEmailRequestConsumer(fastLaneBookingEmailRequestProcessor, fastLaneConsumer(), topic, pollTimeout);
    }

    private Consumer<String, String> fastLaneConsumer() {
        Consumer<String, String> consumer = new KafkaConsumer<>(consumerConfigs());

        consumer.subscribe(Collections.singletonList(topic));

        return consumer;
    }

    private Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();

        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // allows a pool of processes to divide the work of consuming and processing records
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // automatically reset the offset to the earliest offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return props;
    }
}
