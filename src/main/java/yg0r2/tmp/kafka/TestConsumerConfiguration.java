package yg0r2.tmp.kafka;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConsumerConfiguration {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.groupId")
    private String groupId;
    @Value("${kafka.slowLane.topic}")
    private String slowLaneTopic;

    @Bean
    public Consumer<String, String> testConsumer() {
        Consumer<String, String> consumer = new KafkaConsumer<>(testConsumerConfigs());

        consumer.subscribe(Collections.singletonList(slowLaneTopic));

        return consumer;
    }

    private Properties testConsumerConfigs() {
        Properties properties = new Properties();

        // list of host:port pairs used for establishing the initial connections to the Kafka cluster
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        // allows a pool of processes to divide the work of consuming and processing records
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);

        // automatically reset the offset to the earliest offset
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);

        return properties;
    }


}
