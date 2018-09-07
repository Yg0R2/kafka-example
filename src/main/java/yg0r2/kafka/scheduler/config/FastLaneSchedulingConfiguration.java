package yg0r2.kafka.scheduler.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FastLaneSchedulingConfiguration {

    private static final String BEAN_NAME = "fastLaneKafkaMessageRecordConsumer";

    @Value("${kafka.fastLane.poll.interval.ms}")
    private long fastLanePollInterval;
    @Value("${kafka.fastLane.poll.initialDelay.ms}")
    private long fastLaneInitialDelay;
    @Value("${kafka.fastLane.poll.pool.size}")
    private int fastLanePollPoolSize;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ScheduledThreadPoolExecutorFactory scheduledThreadPoolExecutorFactory;

    @Bean("fastLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor fastLaneScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutorFactory.create(applicationContext, BEAN_NAME, fastLanePollPoolSize, fastLaneInitialDelay, fastLanePollInterval);
    }

}
