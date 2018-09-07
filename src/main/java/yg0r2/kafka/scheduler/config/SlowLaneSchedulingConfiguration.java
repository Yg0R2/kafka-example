package yg0r2.kafka.scheduler.config;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SlowLaneSchedulingConfiguration {

    private static final String BEAN_NAME = "slowLaneKafkaMessageRecordConsumer";

    @Value("${kafka.slowLane.poll.interval.ms}")
    private long slowLanePollInterval;
    @Value("${kafka.slowLane.poll.initialDelay.ms}")
    private long slowLaneInitialDelay;
    @Value("${kafka.slowLane.poll.pool.size}")
    private int slowLanePollPoolSize;

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ScheduledThreadPoolExecutorFactory scheduledThreadPoolExecutorFactory;

    @Bean("slowLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor slowLaneScheduledThreadPoolExecutor() {
        return scheduledThreadPoolExecutorFactory.create(applicationContext, BEAN_NAME, slowLanePollPoolSize, slowLaneInitialDelay, slowLanePollInterval);
    }

}
