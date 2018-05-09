package yg0r2.tmp.kafka;

import java.time.Instant;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableKafka
public class SlowLaneConsumerConfig implements SchedulingConfigurer {

    @Value("${kafka.bootstrap-servers}")
    private String bootstrapServers;
    @Value("${kafka.groupId}")
    private String groupId;
    @Value("${kafka.slowLane.topic}")
    private String slowLaneTopic;
    @Value("${kafka.slowLane.triggerTime.ms}")
    private long triggerTimeMs;

    @Autowired
    private SlowLaneListener slowLaneListener;

    @Bean
    public Consumer<String, String> slowLaneConsumer() {
        Consumer<String, String> consumer = new KafkaConsumer<>(slowLaneConfigs());

        consumer.subscribe(Collections.singletonList(slowLaneTopic));

        return consumer;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());

        taskRegistrar.addTriggerTask(
            () -> slowLaneListener.poll(),
            triggerContext -> triggerTask()
        );
    }

    @Bean(destroyMethod = "shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(100);
    }

    private Map<String, Object> slowLaneConfigs() {
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

        props.put(ConsumerConfig.HEARTBEAT_INTERVAL_MS_CONFIG, 10000);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 30000);

        // maximum records per poll
        //props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "10");

        return props;
    }

    private Date triggerTask() {
        Instant nextTriggerTime = Instant.now().plusMillis(triggerTimeMs);

        return Date.from(nextTriggerTime);
    }

}
