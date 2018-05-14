package yg0r2.tmp.kafka;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.CustomizableThreadFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
public class BookingEmailRequestSchedulingConfiguration {

    @Value("${kafka.fastLane.poll.interval.ms}")
    private long fastLanePollInterval;
    @Value("${kafka.fastLane.poll.pool.size}")
    private int fastLanePollPoolSize;
    @Value("${kafka.slowLane.poll.interval.ms}")
    private long slowLanePollInterval;
    @Value("${kafka.slowLane.poll.pool.size}")
    private int slowLanePollPoolSize;


    @Autowired
    @Qualifier(value = "fastLaneBookingEmailRequestConsumer")
    private BookingEmailRequestConsumer fastLaneBookingEmailRequestConsumer;
    @Autowired
    @Qualifier(value = "slowLaneBookingEmailRequestConsumer")
    private BookingEmailRequestConsumer slowLaneBookingEmailRequestConsumer;

    @Bean("fastLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor fastLaneScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor fastLaneExecutor = new ScheduledThreadPoolExecutor(fastLanePollPoolSize);

        fastLaneExecutor.setMaximumPoolSize(fastLanePollPoolSize);
        fastLaneExecutor.scheduleAtFixedRate(
            () -> fastLaneBookingEmailRequestConsumer.poll(),
            0,
            fastLanePollInterval,
            TimeUnit.MILLISECONDS);

        return fastLaneExecutor;
    }

    @Bean("slowLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor slowLaneScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor slowLaneExecutor = new ScheduledThreadPoolExecutor(slowLanePollPoolSize);

        slowLaneExecutor.setMaximumPoolSize(slowLanePollPoolSize);
        slowLaneExecutor.scheduleAtFixedRate(
                () -> slowLaneBookingEmailRequestConsumer.poll(),
                0,
                slowLanePollInterval,
                TimeUnit.MILLISECONDS);

        return slowLaneExecutor;
    }

}
