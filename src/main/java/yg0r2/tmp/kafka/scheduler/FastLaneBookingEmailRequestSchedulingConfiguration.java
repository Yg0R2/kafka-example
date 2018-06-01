package yg0r2.tmp.kafka.scheduler;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import yg0r2.tmp.kafka.consumer.BookingEmailRequestConsumer;

@Configuration
public class FastLaneBookingEmailRequestSchedulingConfiguration {

    @Value("${kafka.fastLane.poll.interval.ms}")
    private long fastLanePollInterval;
    @Value("${kafka.fastLane.poll.initialDelay.ms}")
    private long fastLaneInitialDelay;
    @Value("${kafka.fastLane.poll.pool.size}")
    private int fastLanePollPoolSize;

    @Autowired
    @Qualifier(value = "fastLaneBookingEmailRequestConsumer")
    private BookingEmailRequestConsumer fastLaneBookingEmailRequestConsumer;

    @Bean("fastLaneScheduledThreadPoolExecutor")
    public ScheduledThreadPoolExecutor fastLaneScheduledThreadPoolExecutor() {
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(fastLanePollPoolSize);

        executor.setMaximumPoolSize(fastLanePollPoolSize);
        executor.scheduleAtFixedRate(
            () -> fastLaneBookingEmailRequestConsumer.poll(),
            fastLaneInitialDelay,
            fastLanePollInterval,
            TimeUnit.MILLISECONDS);

        return executor;
    }

}
